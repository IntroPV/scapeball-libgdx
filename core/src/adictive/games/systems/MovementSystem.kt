package adictive.games.systems

import adictive.games.components.MovementComponent
import adictive.games.components.PlayerComponent
import adictive.games.components.TransformComponent
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2

class MovementSystem : IteratingSystem(Family.all(TransformComponent::class.java, MovementComponent::class.java).get()), Reseteable {
    private val tmp = Vector2()

    private val tm: ComponentMapper<TransformComponent>
    private val mm: ComponentMapper<MovementComponent>

    init {

        tm = ComponentMapper.getFor(TransformComponent::class.java)
        mm = ComponentMapper.getFor(MovementComponent::class.java)
    }

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformComponent = tm.get(entity)
        val movementComponent = mm.get(entity)

        transformComponent.lastPos.set(transformComponent.pos)

        movementComponent.velocity.add(movementComponent.accel)


        tmp.set(movementComponent.velocity)

        val velLen = movementComponent.velocity.len()
        if (velLen > PlayerComponent.MAX_VELOCITY) {
            tmp.nor().scl(PlayerComponent.MAX_VELOCITY)
            movementComponent.velocity.nor().scl(PlayerComponent.MAX_VELOCITY)
        }

        if (movementComponent.velocity.len() > movementComponent.friction) {
            //apply friction
            tmp.set(movementComponent.velocity).nor().scl(-movementComponent.friction)
            movementComponent.velocity.add(tmp)
        } else {
            movementComponent.velocity.set(0f, 0f)
        }

        tmp.set(movementComponent.velocity).scl(deltaTime)

        transformComponent.pos.add(tmp.x, tmp.y, 0.0f)

        if (movementComponent.velocity.len() > 0) {
            transformComponent.rotation = movementComponent.velocity.angle()
        }


    }

    override fun reset() {
        // Do nothing. Does not hold game state.
    }
}