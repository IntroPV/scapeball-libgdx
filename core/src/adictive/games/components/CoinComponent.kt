package adictive.games.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class CoinComponent : Component {
    companion object {

        const val WIDTH = 0.5f
        const val HEIGHT = 0.5f

        val textureRegion = TextureRegion(Texture(Gdx.files.internal("data/coin.png")))

        fun addNew(engine: Engine, x: Float, y: Float): Entity {
            val block = create(x, y)

            engine.addEntity(block)
            return block
        }

        fun create(x: Float, y: Float): Entity {
            val block = Entity()

            val textureComponent = TextureComponent()
            textureComponent.region = textureRegion
            block.add(textureComponent)

            val transformComponent = TransformComponent()
            transformComponent.size.set(WIDTH, HEIGHT)
            transformComponent.pos.set(x, y, 0f)
            block.add(transformComponent)

            block.add(CoinComponent())

            val boundsComponent = BoundsComponent()
            boundsComponent.bounds.set(0f, 0f, WIDTH, HEIGHT)
            block.add(boundsComponent)

            val lightComponent = LightComponent()
            lightComponent.color = Color(1f, 1f, 0f, 0.4f)
            block.add(lightComponent)
            return block
        }
    }
}
