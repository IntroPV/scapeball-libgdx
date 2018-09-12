package adictive.games.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

class WallComponent : Component {
    companion object {

        private val blackTextureRegion = TextureRegion(Texture(Gdx.files.internal("data/blackBox.png")))
        private val chainTextureRegion = TextureRegion(Texture(Gdx.files.internal("data/chainBox.png")))

        const val FLAG_CHAINBOX = 0x1

        fun addNewWall(engine: Engine, x: Int, y: Int, flag: Int): Entity {
            val block = create(x, y, flag)

            engine.addEntity(block)
            return block
        }

        fun create(x: Int, y: Int, flag: Int): Entity {
            val block = Entity()
            block.flags = flag

            val textureComponent = TextureComponent()

            if (flag and FLAG_CHAINBOX == FLAG_CHAINBOX) {
                textureComponent.region = chainTextureRegion
            } else {
                textureComponent.region = blackTextureRegion
            }

            block.add(textureComponent)

            val transformComponent = TransformComponent()
            transformComponent.size.set(1f, 1f)
            transformComponent.pos.set(x.toFloat(), y.toFloat(), 0f)
            block.add(transformComponent)

            val boundsComponent = BoundsComponent()
            boundsComponent.bounds.set(0f, 0f, 1f, 1f)
            block.add(boundsComponent)

            block.add(WallComponent())
            return block
        }
    }
}
