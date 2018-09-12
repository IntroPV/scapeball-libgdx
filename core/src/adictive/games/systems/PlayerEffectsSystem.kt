package adictive.games.systems

import adictive.games.components.PlayerComponent
import adictive.games.components.TransformComponent
import adictive.games.utils.Families
import com.badlogic.ashley.core.*
import com.badlogic.gdx.utils.Array

/**
 * Created by arielalvarez on 2/4/18.
 */

class PlayerEffectsSystem : EntitySystem(), Reseteable {
    private val players = Array<Entity>()
    private val playerComp = ComponentMapper.getFor<PlayerComponent>(PlayerComponent::class.java)
    private val tc = ComponentMapper.getFor<TransformComponent>(TransformComponent::class.java)

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

    override fun update(deltaTime: Float) {
        for (entity in players) {
            val player = playerComp.get(entity)
            val body = tc.get(entity)

            player.trailX[4] = player.trailX[3]
            player.trailX[3] = player.trailX[2]
            player.trailX[2] = player.trailX[1]
            player.trailX[1] = player.trailX[0]
            player.trailX[0] = body.pos.x

            player.trailY[4] = player.trailY[3]
            player.trailY[3] = player.trailY[2]
            player.trailY[2] = player.trailY[1]
            player.trailY[1] = player.trailY[0]
            player.trailY[0] = body.pos.y
        }
    }

    override fun reset() {
        players.clear()
    }
}
