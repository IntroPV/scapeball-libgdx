package adictive.games.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class WinComponent : Component {
    companion object {

        val textureRegion = TextureRegion(Texture(Gdx.files.internal("data/winBox.png")))
        const val WIDTH = 1f
        const val HEIGHT = 1f

        fun addNew(engine: Engine, x: Float, y: Float): Entity {
            val block = create(x.toInt(), y.toInt())

            engine.addEntity(block)
            return block
        }

        fun create(x: Int, y: Int): Entity {
            val block = Entity()

            val textureComponent = TextureComponent()

            textureComponent.region = textureRegion
            block.add(textureComponent)

            val transformComponent = TransformComponent()
            transformComponent.size.set(WIDTH, HEIGHT)
            transformComponent.pos.set(x.toFloat(), y.toFloat(), 0f)
            block.add(transformComponent)

            val boundsComponent = BoundsComponent()
            boundsComponent.bounds.set(0f, 0f, WIDTH, HEIGHT)
            block.add(boundsComponent)

            val lightComponent = LightComponent()
            lightComponent.color = Color(0f, 1f, 0f, 0.4f)
            lightComponent.shape = LightComponent.CIRCULAR
            lightComponent.radius = 4f
            block.add(lightComponent)

            block.add(WinComponent())
            return block
        }
    }

}
