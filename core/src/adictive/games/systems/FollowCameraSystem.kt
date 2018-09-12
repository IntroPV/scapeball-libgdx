package adictive.games.systems

import adictive.games.components.CameraComponent
import adictive.games.components.TransformComponent
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

class FollowCameraSystem : IteratingSystem(Family.all(TransformComponent::class.java, CameraComponent::class.java).get()), Reseteable {
    private val tm: ComponentMapper<TransformComponent>
    private val cm: ComponentMapper<CameraComponent>

    init {
        cm = ComponentMapper.getFor(CameraComponent::class.java)
        tm = ComponentMapper.getFor(TransformComponent::class.java)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val cam = cm.get(entity)
        val target = tm.get(entity)

        cam.camera!!.position.x = target.pos.x
        cam.camera!!.position.y = target.pos.y
    }

    override fun reset() {
        // Do nothing. Does not hold game state.
    }
}
