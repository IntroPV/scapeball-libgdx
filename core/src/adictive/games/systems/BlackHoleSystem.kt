package adictive.games.systems

import adictive.games.components.AttractionComponent
import adictive.games.components.BlackHoleComponent
import adictive.games.components.MovementComponent
import adictive.games.components.TransformComponent
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2

class BlackHoleSystem : IteratingSystem(HOLE_FAMILY), Reseteable {

    private val tmp = Vector2()

    override fun processEntity(attractor: Entity, deltaTime: Float) {
        val attractables = engine.getEntitiesFor(ATTRACTABLE_FAMILY)
        val tc = attractor.getComponent<TransformComponent>(TransformComponent::class.java)
        val centerX = tc.pos.x + tc.size.x / 2
        val centerY = tc.pos.y + tc.size.y / 2
        val attraction = holeMapper.get(attractor).attraction

        for (e in attractables) {
            attract(e, centerX, centerY, attraction, deltaTime)
        }
    }

    private fun attract(entity: Entity, centerX: Float, centerY: Float, attraction: Float, deltaTime: Float) {
        val mv = movementComponent.get(entity)
        val tc = entity.getComponent<TransformComponent>(TransformComponent::class.java)
        tmp.set(centerX, centerY)
        tmp.sub(tc.pos.x + tc.size.x / 2, tc.pos.y + tc.size.y / 2)
        val distance = tmp.len()
        tmp.nor().scl(attraction + Math.pow((1 + attraction / distance).toDouble(), 3.0).toFloat()).scl(deltaTime)
        mv.velocity.add(tmp)
    }

    override fun reset() {
        // Do nothing. This system does not hold state
    }

    companion object {
        private val movementComponent = ComponentMapper.getFor<MovementComponent>(MovementComponent::class.java)
        private val holeMapper = ComponentMapper.getFor<BlackHoleComponent>(BlackHoleComponent::class.java)
        private val HOLE_FAMILY = Family.all(BlackHoleComponent::class.java, TransformComponent::class.java).get()
        private val ATTRACTABLE_FAMILY = Family.all(AttractionComponent::class.java, MovementComponent::class.java).get()
    }
}
