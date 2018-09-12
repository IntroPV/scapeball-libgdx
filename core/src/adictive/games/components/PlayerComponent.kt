package adictive.games.components

import adictive.games.SquareWorld
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class PlayerComponent : Component {

    val trailX = FloatArray(5)
    val trailY = FloatArray(5)

    companion object {
        val MAX_VELOCITY = 8f
        val ACCEL = 2f
        val WIDTH = 0.5f
        val HEIGHT = 0.5f
        val LAYER = 3f
        val playerTexture = Texture(Gdx.files.internal("data/player_sheet.png"))

        private val textureRegion = TextureRegion(playerTexture, 0, 0, 24, 24)

        val trail1 = TextureRegion(playerTexture, 24, 0, 24, 24)
        val trail2 = TextureRegion(playerTexture, 24 * 2, 0, 24, 24)
        val trail3 = TextureRegion(playerTexture, 24 * 3, 0, 24, 24)
        val trail4 = TextureRegion(playerTexture, 24 * 4, 0, 24, 24)

        fun addNew(world: SquareWorld, engine: Engine, x: Float, y: Float) {
            val player = create(world, x, y)
            engine.addEntity(player)
        }

        fun create(world: SquareWorld, x: Float, y: Float): Entity {
            val player = Entity()

            player.add(PlayerComponent())

            val cameraComponent = CameraComponent()
            cameraComponent.camera = world.camera
            player.add(cameraComponent)

            val textureComponent = TextureComponent()
            textureComponent.region = textureRegion
            //        textureRegion.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            player.add(textureComponent)

            val transformComponent = TransformComponent()
            transformComponent.size.set(WIDTH, HEIGHT)
            transformComponent.pos.set(x, y, LAYER)
            player.add(transformComponent)

            val boundsComponent = BoundsComponent()
            boundsComponent.bounds.set(0f, 0f, 0.5f, 0.5f)
            player.add(boundsComponent)

            val lightComponent = LightComponent()
            lightComponent.color = Color(1f, 0f, 0f, 0.4f)
            lightComponent.radius = 1.5f
            player.add(lightComponent)

            player.add(MovementComponent())
            player.add(AttractionComponent())
            return player
        }
    }
}
