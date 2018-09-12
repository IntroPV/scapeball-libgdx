package adictive.games.systems

import adictive.games.components.MovementComponent
import adictive.games.components.PlayerComponent
import adictive.games.utils.Families
import com.badlogic.ashley.core.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array


class PlayerInputSystem : EntitySystem(), Reseteable {
    private val players = Array<Entity>()
    private val movementMapper = ComponentMapper.getFor<MovementComponent>(MovementComponent::class.java)

    private val touch = Vector3(0f, 0f, 0f)
    private val touchOrigin = Vector3(-1f, -1f, -1f)

    override fun update(deltaTime: Float) {
        for (player in players) {
            val movementComponent = movementMapper.get(player)

            if (Gdx.input.justTouched()) {
                touchOrigin.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            }

            if (Gdx.input.isTouched && touchOrigin.z != -1f) {
                touch.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)

                touch.sub(touchOrigin.x, touchOrigin.y, 0f).nor()

                movementComponent.accel.set(touch.x, -touch.y).nor().scl(PlayerComponent.ACCEL)
            } else {
                movementComponent.accel.set(0f, 0f)
            }
        }
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        engine!!.addEntityListener(
                Families.PLAYER,
                object : EntityListener {
                    override fun entityAdded(entity: Entity) {
                        players.add(entity)
                    }

                    override fun entityRemoved(entity: Entity) {
                        players.removeValue(entity, true)
                    }
                }
        )
    }

    override fun reset() {
        touch.setZero()
        touchOrigin.set(-1f, -1f, -1f)
        players.clear()
    }
}
