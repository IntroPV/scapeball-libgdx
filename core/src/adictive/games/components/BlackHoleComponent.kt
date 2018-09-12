package adictive.games.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class BlackHoleComponent : Component {
    var attraction: Float = 0.toFloat()

    companion object {
        const val SIDE = 1.5f
        val textureRegion = TextureRegion(Texture(Gdx.files.internal("data/blackHole.png")))

        fun addNew(engine: Engine, x: Float, y: Float, attraction: Float): Entity {
            val block = create(x, y, attraction)
            engine.addEntity(block)
            return block
        }

        fun create(x: Float, y: Float, attraction: Float): Entity {
            val block = Entity()

            val textureComponent = TextureComponent()
            textureComponent.region = textureRegion
            block.add(textureComponent)

            val transformComponent = TransformComponent()
            transformComponent.size.set(SIDE, SIDE)
            transformComponent.pos.set(x, y, 0f)
            block.add(transformComponent)

            val enemyComponent = BlackHoleComponent()
            enemyComponent.attraction = attraction
            block.add(enemyComponent)

            val boundsComponent = BoundsComponent()
            boundsComponent.bounds.set(0f, 0f, 1f, 1f)
            block.add(boundsComponent)

            val lightComponent = LightComponent()
            lightComponent.color = Color(0.05f, 0.05f, 0.05f, 0.8f)
            lightComponent.radius = 4f
            block.add(lightComponent)

            return block
        }
    }
}
