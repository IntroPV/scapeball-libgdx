package adictive.games.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

class EnemyComponent : Component {
    val direction = Vector2(3f, 0f)
    var vel = 10f
    var posInLine = 0f
    var initialPosInLine = 0f
    val start = Vector2()
    val end = Vector2()

    fun resetDirection() {
        direction.set(end.x - start.x, end.y - start.y)
    }

    companion object {
        val textureRegion = TextureRegion(Texture(Gdx.files.internal("data/enemy.png")))

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
            transformComponent.size.set(0.5f, 0.5f)
            transformComponent.pos.set(x - transformComponent.size.x / 2, y - transformComponent.size.y / 2, 0f)
            block.add(transformComponent)

            val enemyComponent = EnemyComponent()
            block.add(enemyComponent)

            val boundsComponent = BoundsComponent()
            boundsComponent.bounds.set(0f, 0f, 0.5f, 0.5f)
            block.add(boundsComponent)

            return block
        }

        fun addNew(engine: Engine, x: Float, y: Float, posInLine: Float, startX: Float, startY: Float, endX: Float, endY: Float): Entity {
            val enemy = addNew(engine, x, y)
            val ec = enemy.getComponent<EnemyComponent>(EnemyComponent::class.java)
            ec.initialPosInLine = posInLine
            ec.posInLine = posInLine
            ec.start.x = startX
            ec.start.y = startY
            ec.end.x = endX
            ec.end.y = endY

            return enemy
        }
    }
}
