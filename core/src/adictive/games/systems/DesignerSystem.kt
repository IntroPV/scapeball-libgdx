package adictive.games.systems

import adictive.games.SquareWorld
import adictive.games.TiledMap
import adictive.games.components.*
import adictive.games.components.WallComponent.Companion.FLAG_CHAINBOX
import adictive.games.level.LevelWriter
import adictive.games.screens.PlayScreen
import adictive.games.utils.GameData
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3


class DesignerSystem(private val world: SquareWorld, private val screen: PlayScreen) : EntitySystem(11), Reseteable {

    companion object {

        const val NAVIGATION_MODE = 0
        const val ENEMY_END_POSITION_MODE = 2
        const val ENEMY_INITIAL_POSITION_MODE = 3

        private val UP = Vector3(0f, 1f, 0f)
        private val DOWN = Vector3(0f, -1f, 0f)
        private val LEFT = Vector3(-1f, 0f, 0f)
        private val RIGHT = Vector3(1f, 0f, 0f)

        private val LIGHT_RED = Color(-0x5c4801)
    }

    private val shapeRenderer = ShapeRenderer()
    private val batch = SpriteBatch()

    private val transformable = Family.all(BoundsComponent::class.java, TransformComponent::class.java).get()
    private val enemiesFamily = Family.all(EnemyComponent::class.java!!).get()

    private val lastTouch = Vector3(0f, 0f, 0f)
    private val touch = Vector3(0f, 0f, 0f)
    private val cursor = Vector3(0f, 0f, 0f)

    private val brushes: Array<Brush>

    private val levelWriter = LevelWriter()

    private var state = 0

    init {

        this.brushes = arrayOf(WallBrush(0, 0, 0), WallBrush(0, 0, FLAG_CHAINBOX), WinBrush(0, 0), EnemyBrush(0f, 0f), CoinBrush(0f, 0f), FloorBrush(0, 0, FloorEffectComponent.ICE), FloorBrush(0, 0, FloorEffectComponent.DIRT), FloorBrush(0, 0, FloorEffectComponent.ACCELERATOR), SpikeBrush(0, 0, 0f), SpikeBrush(0, 0, 90f), SpikeBrush(0, 0, 180f), SpikeBrush(0, 0, 270f), BlackHoleBrush(0, 0, 4f), PlayerBrush(world, 0f, 0f))
    }

    override fun update(deltaTime: Float) {
        updateCursor()
        updateCameraPosition()
        updateDesignState()
        drawHelpers()
    }

    private fun updateCursor() {
        cursor.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        world.camera.unproject(cursor)

        if (Gdx.input.justTouched()) {
            lastTouch.set(cursor)
        }

        if (Gdx.input.isTouched) {
            touch.set(cursor)
        }
    }

    private fun updateDesignState() {
        val brush = brushes[state]

        if (brush.shouldBeDelegatedTo()) {
            brush.process()
            return
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            state -= 1
            if (state < 0) state = brushes.size - 1
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            state += 1
            if (state >= brushes.size) state = 0
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            drawBrushPreview(brush)
        }

        applyActions(brush)
    }

    private fun drawHelpers() {
        val brush = brushes[state]

        shapeRenderer.projectionMatrix = world.camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.FIREBRICK

        drawGrid()

        if (brush.shouldBeDelegatedTo()) {
            brush.drawHelpers()
        }

        drawEnemyTrayectories()

        shapeRenderer.end()
    }

    private fun drawEnemyTrayectories() {
        val enemies = engine.getEntitiesFor(enemiesFamily)
        shapeRenderer.color = LIGHT_RED
        for (e in enemies) {
            val ec = e.getComponent<EnemyComponent>(EnemyComponent::class.java)
            if (ec.end.x != 0f && ec.end.y != 0f) {
                shapeRenderer.line(ec.start.x, ec.start.y, ec.end.x, ec.end.y)
            }
        }
    }

    private fun applyActions(brush: Brush) {

        if (Gdx.input.isTouched) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                brush.erase()
            } else {
                brush.paint()
            }
        }

        // Paint a single time in order to avoid generating too many elements
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            brush.paint()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            saveLevel()
        }
    }

    private fun drawBrushPreview(brush: Brush) {
        batch.projectionMatrix = world.camera.combined
        batch.begin()
        batch.setColor(1f, 1f, 1f, 0.5f)

        val tc = brush.size
        batch.draw(
                brush.textureRegion!!,
                cursor.x - tc.size.x / 2,
                cursor.y - tc.size.y / 2,
                tc.size.x / 2,
                tc.size.y / 2,
                tc.size.x,
                tc.size.y,
                tc.scale.x,
                tc.scale.y,
                tc.rotation
        )

        batch.end()
    }

    private fun saveLevel() {
        levelWriter.write(engine, GameData.currentLevel)
    }

    private fun drawGrid() {
        shapeRenderer.color = Color.LIGHT_GRAY

        for (x in 0 until world.width) {
            shapeRenderer.line(x.toFloat(), 0f, x.toFloat(), world.height.toFloat())
        }

        for (y in 0 until world.height) {
            shapeRenderer.line(0f, y.toFloat(), world.width.toFloat(), y.toFloat())
        }
    }

    private fun updateCameraPosition() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            world.camera.position.add(UP)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            world.camera.position.add(DOWN)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            world.camera.position.add(LEFT)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            world.camera.position.add(RIGHT)
        }
    }

    override fun reset() {
        // DO nothing. Does not hold game state.
    }

    internal open inner class Brush(protected var entity: Entity) {

        val size: TransformComponent
            get() = entity.getComponent(TransformComponent::class.java)

        val textureRegion: TextureRegion?
            get() = entity.getComponent<TextureComponent>(TextureComponent::class.java).region

        open fun paint() {

        }

        open fun shouldBeDelegatedTo(): Boolean {
            return false
        }

        open fun process() {

        }

        open fun drawHelpers() {

        }

        fun erase() {
            val entities = engine.getEntitiesFor(transformable)
            val body = Rectangle()
            for (entity in entities) {
                val tc = entity.getComponent<TransformComponent>(TransformComponent::class.java)
                body.set(tc.pos.x, tc.pos.y, tc.size.x, tc.size.y)

                if (body.contains(touch.x, touch.y)) {
                    engine.removeEntity(entity)
                }
            }
        }
    }

    internal inner class CoinBrush(x: Float, y: Float) : Brush(CoinComponent.create(x, y)) {

        override fun paint() {
            val dstX = cursor.x - CoinComponent.WIDTH / 2
            val dstY = cursor.y - CoinComponent.WIDTH / 2

            if (world.tiledMap.getEntity(dstX.toInt(), dstY.toInt(), TiledMap.COIN) != null) return

            CoinComponent.addNew(engine, dstX, dstY)
        }
    }

    internal inner class EnemyBrush(x: Float, y: Float) : Brush(EnemyComponent.create(x, y)) {

        private var enemy: Entity? = null
        private var editionMode = NAVIGATION_MODE

        override fun shouldBeDelegatedTo(): Boolean {
            return editionMode != NAVIGATION_MODE
        }

        override fun drawHelpers() {
            if (editionMode == ENEMY_END_POSITION_MODE) {
                val ec = enemy!!.getComponent<EnemyComponent>(EnemyComponent::class.java)
                shapeRenderer.line(ec.start.x, ec.start.y, cursor.x, cursor.y)
            } else if (editionMode == ENEMY_INITIAL_POSITION_MODE) {
                val ec = enemy!!.getComponent<EnemyComponent>(EnemyComponent::class.java)
                shapeRenderer.line(ec.start.x, ec.start.y, ec.end.x, ec.end.y)
            }
        }

        override fun process() {
            if (editionMode == ENEMY_END_POSITION_MODE && (Gdx.input.isKeyJustPressed(Input.Keys.F) || Gdx.input.justTouched())) {
                val ec = enemy!!.getComponent<EnemyComponent>(EnemyComponent::class.java)
                ec.end.set(cursor.x, cursor.y)
                editionMode = ENEMY_INITIAL_POSITION_MODE
            } else if (editionMode == ENEMY_INITIAL_POSITION_MODE) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    val ec = enemy!!.getComponent<EnemyComponent>(EnemyComponent::class.java)
                    ec.resetDirection()
                    EnemySystem.moveEnemyToPos(enemy!!, ec, ec.posInLine - 0.5f)
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                    val ec = enemy!!.getComponent<EnemyComponent>(EnemyComponent::class.java)
                    ec.resetDirection()
                    EnemySystem.moveEnemyToPos(enemy!!, ec, ec.posInLine + 0.5f)
                    ec.initialPosInLine = ec.posInLine + 0.5f
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                    enemy = null
                    editionMode = NAVIGATION_MODE
                }
            }
        }

        override fun paint() {
            this.enemy = EnemyComponent.addNew(engine, cursor.x, cursor.y)
            enemy!!.getComponent<EnemyComponent>(EnemyComponent::class.java).start.set(cursor.x, cursor.y)
            editionMode = ENEMY_END_POSITION_MODE
        }
    }

    /**************************************/
    /************** BRUSHES ***************/
    /**************************************/

    internal inner class WallBrush(x: Int, y: Int, private val flag: Int) : Brush(WallComponent.create(x, y, flag)) {

        override fun paint() {
            val x = cursor.x.toInt()
            val y = cursor.y.toInt()

            if (world.tiledMap.getEntity(x, y, TiledMap.WALL) != null) return

            WallComponent.addNewWall(engine, x, y, flag)
        }
    }

    internal inner class WinBrush(x: Int, y: Int) : Brush(WinComponent.create(x, y)) {

        override fun paint() {
            val x = cursor.x.toInt()
            val y = cursor.y.toInt()

            if (world.tiledMap.getEntity(x, y, TiledMap.WIN) != null) return

            WinComponent.addNew(engine, x.toFloat(), y.toFloat())
        }
    }

    internal inner class FloorBrush(x: Int, y: Int, private val type: Byte) : Brush(FloorEffectComponent.create(x, y, type)) {

        override fun paint() {
            val x = cursor.x.toInt()
            val y = cursor.y.toInt()

            if (world.tiledMap.getEntity(x, y, TiledMap.FLOOR) != null) return

            FloorEffectComponent.addNew(engine, x.toFloat(), y.toFloat(), type)
        }
    }

    internal inner class PlayerBrush(world: SquareWorld, x: Float, y: Float) : Brush(PlayerComponent.create(world, x, y)) {

        override fun paint() {
            PlayerComponent.addNew(world, engine, cursor.x - PlayerComponent.WIDTH / 2, cursor.y - PlayerComponent.WIDTH / 2)
        }
    }

    internal inner class SpikeBrush(x: Int, y: Int, private val rotation: Float) : Brush(SpikeComponent.create(x, y, rotation)) {

        override fun paint() {
            val x = cursor.x.toInt()
            val y = cursor.y.toInt()

            if (world.tiledMap.getEntity(x, y, TiledMap.SPIKE) != null) return

            SpikeComponent.addNew(engine, x, y, rotation)
        }
    }

    internal inner class BlackHoleBrush(x: Int, y: Int, private val attraction: Float) : Brush(BlackHoleComponent.create(x.toFloat(), y.toFloat(), attraction)) {

        override fun paint() {
            val x = cursor.x.toInt() - BlackHoleComponent.SIDE / 2 + 0.5f
            val y = cursor.y.toInt() - BlackHoleComponent.SIDE / 2 + 0.5f

            if (world.tiledMap.getEntity(x.toInt(), y.toInt(), TiledMap.HOLE) != null) return

            BlackHoleComponent.addNew(engine, x, y, attraction)
        }
    }


}
