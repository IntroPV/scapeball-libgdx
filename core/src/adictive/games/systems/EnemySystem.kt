package adictive.games.systems

import adictive.games.components.EnemyComponent
import adictive.games.components.TransformComponent
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

class EnemySystem : IteratingSystem(Family.all(EnemyComponent::class.java, TransformComponent::class.java).get()), Reseteable {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        moveEnemy(entity, deltaTime)
    }

    override fun reset() {
        // Do nothing. Does not hold game state
    }

    companion object {
        private val transformMapper = ComponentMapper.getFor<TransformComponent>(TransformComponent::class.java)
        private val enemyMapper = ComponentMapper.getFor<EnemyComponent>(EnemyComponent::class.java)

        fun moveEnemy(entity: Entity, deltaTime: Float) {

            val ec = enemyMapper.get(entity)

            val nextPos = ec.posInLine + ec.vel * deltaTime

            ec.resetDirection()

            if (nextPos > ec.direction.len() || nextPos < 0) {
                ec.vel *= -1f
                moveEnemyToPos(entity, ec, ec.posInLine + ec.vel * deltaTime)
            } else {
                moveEnemyToPos(entity, ec, nextPos)
            }
        }

        fun moveEnemyToPos(entity: Entity, ec: EnemyComponent, nextPos: Float) {
            val tc = transformMapper.get(entity)
            tc.pos.set(ec.start.x, ec.start.y, 0f)
            ec.direction.nor()
            ec.direction.scl(nextPos)
            ec.posInLine = nextPos
            tc.pos.add(ec.direction.x - tc.size.x / 2, ec.direction.y - tc.size.y / 2, 0f)
        }
    }
}
