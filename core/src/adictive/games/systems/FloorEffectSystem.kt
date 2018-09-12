package adictive.games.systems

import adictive.games.SquareWorld
import adictive.games.TiledMap
import adictive.games.components.FloorEffectComponent
import adictive.games.components.MovementComponent
import adictive.games.components.TransformComponent
import adictive.games.utils.Families
import com.badlogic.ashley.core.*
import com.badlogic.gdx.utils.Array


class FloorEffectSystem(private val world: SquareWorld) : EntitySystem(), Reseteable {
    private val players = Array<Entity>()
    private val tm: ComponentMapper<TransformComponent>
    private val mm: ComponentMapper<MovementComponent>

    init {
        tm = ComponentMapper.getFor(TransformComponent::class.java)
        mm = ComponentMapper.getFor(MovementComponent::class.java)
    }

    override fun update(deltaTime: Float) {
        for (player in players) {
            val mc = mm.get(player)
            val tc = tm.get(player)

            val x = (tc.pos.x + tc.size.x / 2).toInt()
            val y = (tc.pos.y + tc.size.y / 2).toInt()

            val floor = world.tiledMap.getEntity(x, y, TiledMap.FLOOR)

            if (floor == null) {
                mc.friction = NORMAL_FRICTION
            } else if (floor.flags == FloorEffectComponent.ICE.toInt()) {
                mc.friction = ICE_FRICTION
            } else if (floor.flags == FloorEffectComponent.DIRT.toInt()) {
                mc.friction = DIRT_FRICTION
            } else if (floor.flags == FloorEffectComponent.ACCELERATOR.toInt()) {
                mc.friction = ACCELERATOR_FRICTION
            }
        }
    }

    override fun reset() {
        players.clear()
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

    companion object {

        private val ICE_FRICTION = 0f
        private val DIRT_FRICTION = 1.8f
        private val NORMAL_FRICTION = 0.2f
        private val ACCELERATOR_FRICTION = -11f
    }
}