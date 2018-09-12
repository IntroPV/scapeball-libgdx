package adictive.games.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class FloorEffectComponent : Component {
    companion object {
        val iceRegion = TextureRegion(Texture(Gdx.files.internal("data/iceFloor.png")))
        val dirtRegion = TextureRegion(Texture(Gdx.files.internal("data/dirtFloor.png")))
        val accelRegion = TextureRegion(Texture(Gdx.files.internal("data/floorAccel.png")))
        val WIDTH = 1f
        val HEIGHT = 1f

        val ICE: Byte = 1
        val DIRT: Byte = 2
        val ACCELERATOR: Byte = 3

        fun addNew(engine: Engine, x: Float, y: Float, type: Byte): Entity {
            val block = create(x.toInt(), y.toInt(), type)
            engine.addEntity(block)
            return block
        }

        fun create(x: Int, y: Int, type: Byte): Entity {
            val block = Entity()
            block.flags = type.toInt()

            val textureComponent = TextureComponent()

            when (type) {
                ICE -> textureComponent.region = iceRegion
                DIRT -> textureComponent.region = dirtRegion
                ACCELERATOR -> textureComponent.region = accelRegion
            }

            block.add(textureComponent)

            val transformComponent = TransformComponent()
            transformComponent.size.set(WIDTH, HEIGHT)
            transformComponent.pos.set(x.toFloat(), y.toFloat(), 0f)
            block.add(transformComponent)

            val boundsComponent = BoundsComponent()
            boundsComponent.bounds.set(0f, 0f, WIDTH, HEIGHT)
            block.add(boundsComponent)

            block.add(FloorEffectComponent())
            return block
        }
    }
}
