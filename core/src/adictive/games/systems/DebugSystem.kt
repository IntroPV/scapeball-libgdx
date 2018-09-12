package adictive.games.systems


import adictive.games.SquareWorld
import adictive.games.components.BoundsComponent
import adictive.games.components.TransformComponent
import adictive.games.screens.PlayScreen
import adictive.games.utils.GameData
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class DebugSystem(private val world: SquareWorld, private val screen: PlayScreen) : EntitySystem(), Reseteable {
    private val shapeRenderer: ShapeRenderer

    private val boundsFamily = Family.all(TransformComponent::class.java, BoundsComponent::class.java).get()

    init {
        this.shapeRenderer = ShapeRenderer()
    }

    override fun update(deltaTime: Float) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            world.camera.zoom += 0.1f
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            world.camera.zoom -= 0.1f
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            GameData.decrementCurrentLevel()
            screen.state = PlayScreen.DEBUG_CHANGE_LEVEL
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            GameData.incrementCurrentLevel()
            screen.state = PlayScreen.DEBUG_CHANGE_LEVEL
        }

        shapeRenderer.projectionMatrix = world.camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)

        val walls = engine.getEntitiesFor(boundsFamily)

        shapeRenderer.color = Color.GREEN

        for (wall in walls) {
            renderBounds(wall)
        }

        shapeRenderer.end()

    }

    private fun renderBounds(entity: Entity) {
        val tr = entity.getComponent<TransformComponent>(TransformComponent::class.java)
        val bc = entity.getComponent<BoundsComponent>(BoundsComponent::class.java)
        shapeRenderer.rect(tr.pos.x + bc.bounds.x, tr.pos.y + bc.bounds.y, bc.bounds.width, bc.bounds.height)
    }

    override fun reset() {
        // do nothing. does not hold game state
    }
}
