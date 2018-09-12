package adictive.games.systems

import adictive.games.SquareWorld
import adictive.games.components.PlayerComponent
import adictive.games.components.TextureComponent
import adictive.games.components.TransformComponent
import adictive.games.utils.SortedOnInsertList
import com.badlogic.ashley.core.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class RenderingSystem(private val world: SquareWorld) : EntitySystem(10), Reseteable {

    private val renderQueue: SortedOnInsertList<Entity>

    private val transformMapper: ComponentMapper<TransformComponent>
    private val textureMapper: ComponentMapper<TextureComponent>
    private val batch: SpriteBatch

    init {
        this.batch = SpriteBatch()
        textureMapper = ComponentMapper.getFor(TextureComponent::class.java)
        transformMapper = ComponentMapper.getFor(TransformComponent::class.java)

        renderQueue = SortedOnInsertList(512,
                Comparator { entityA, entityB ->
                    Math.signum(
                            transformMapper.get(entityA).pos.z - transformMapper.get(entityB).pos.z
                    ).toInt()
                }
        )
    }

    override fun reset() {
        renderQueue.clear()
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        resize(world.camera, Gdx.graphics.width, Gdx.graphics.height)
        engine!!.addEntityListener(
                FAMILY,
                object : EntityListener {
                    override fun entityAdded(entity: Entity) {
                        renderQueue.add(entity)
                    }

                    override fun entityRemoved(entity: Entity) {
                        renderQueue.remove(entity)
                    }
                }
        )
    }

    override fun update(deltaTime: Float) {
        renderEntities()
    }

    private fun renderEntity(entity: Entity) {
        val textureRegion = textureMapper.get(entity).region
        val transformComponent = transformMapper.get(entity)

        val pc = entity.getComponent<PlayerComponent>(PlayerComponent::class.java)
        if (pc != null) {
            renderPlayerEffects(transformComponent, pc)
        }

        drawElement(
                transformComponent.pos.x,
                transformComponent.pos.y,
                textureRegion,
                transformComponent)
    }

    private fun drawElement(x: Float, y: Float, textureRegion: TextureRegion?, transformComponent: TransformComponent) {
        this.batch.draw(
                textureRegion!!,
                x,
                y,
                transformComponent.size.x / 2,
                transformComponent.size.y / 2,
                transformComponent.size.x,
                transformComponent.size.y,
                transformComponent.scale.x,
                transformComponent.scale.y,
                transformComponent.rotation
        )
    }

    private fun renderPlayerEffects(tc: TransformComponent, pc: PlayerComponent) {
        drawElement(pc.trailX[1], pc.trailY[1], PlayerComponent.trail1, tc)
        drawElement(pc.trailX[2], pc.trailY[2], PlayerComponent.trail2, tc)
        drawElement(pc.trailX[3], pc.trailY[3], PlayerComponent.trail3, tc)
        drawElement(pc.trailX[4], pc.trailY[4], PlayerComponent.trail4, tc)
    }

    fun resize(w: Int, h: Int) {
        resize(world.camera, w, h)
    }

    private fun renderEntities() {
        world.camera.update()
        batch.projectionMatrix = world.camera.combined

        batch.begin()
        for (entity in renderQueue) {
            renderEntity(entity)
        }

        batch.end()
    }

    companion object {

        const val VIEWPORT_WIDTH_MTS = 15
        const val VIEWPORT_HEIGHT_MTS = 15
        val FAMILY = Family.all(TextureComponent::class.java, TransformComponent::class.java).get()

        fun resize(cam: OrthographicCamera, deviceWidth: Int, deviceHeight: Int) {

            val width = deviceWidth / Gdx.graphics.density
            val height = deviceHeight / Gdx.graphics.density

            if (width < height) {
                cam.viewportWidth = VIEWPORT_WIDTH_MTS.toFloat()
                cam.viewportHeight = VIEWPORT_HEIGHT_MTS * (height / width)
                cam.zoom = 200f / height
            } else {
                cam.viewportWidth = VIEWPORT_WIDTH_MTS * (width / height)
                cam.viewportHeight = VIEWPORT_HEIGHT_MTS.toFloat()
                cam.zoom = 400f / width
            }
        }
    }
}
