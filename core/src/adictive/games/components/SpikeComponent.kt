package adictive.games.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class SpikeComponent : Component {
    companion object {
        val textureRegion = TextureRegion(Texture(Gdx.files.internal("data/spikes.png")))

        fun create(x: Int, y: Int, rotation: Float): Entity {
            val block = Entity()

            val textureComponent = TextureComponent()
            textureComponent.region = textureRegion
            block.add(textureComponent)

            val transformComponent = TransformComponent()
            transformComponent.size.set(1f, 1f)
            transformComponent.pos.set(x.toFloat(), y.toFloat(), 0f)
            transformComponent.rotation = rotation
            block.add(transformComponent)

            block.add(SpikeComponent())

            val boundsComponent = BoundsComponent()
            when (rotation.toInt()) {
                0 -> boundsComponent.bounds.set(0f, 0f, 0.5f, 1f)
                90 -> boundsComponent.bounds.set(0f, 0f, 1f, 0.5f)
                180 -> boundsComponent.bounds.set(0.5f, 0f, 0.5f, 1f)
                270 -> boundsComponent.bounds.set(0f, 0.5f, 1f, 0.5f)
            }
            block.add(boundsComponent)

            return block
        }

        fun addNew(engine: Engine, x: Int, y: Int, rotation: Float) {
            engine.addEntity(create(x, y, rotation))
        }
    }
}
