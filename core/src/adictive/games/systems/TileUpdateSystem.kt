package adictive.games.systems

import adictive.games.SquareWorld
import adictive.games.components.TransformComponent
import adictive.games.utils.Families
import com.badlogic.ashley.core.*

class TileUpdateSystem(private val world: SquareWorld) : EntitySystem(), Reseteable {

    private val transformMapper = ComponentMapper.getFor<TransformComponent>(TransformComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        engine!!.addEntityListener(Families.BOUNDS, object : EntityListener {
            override fun entityAdded(entity: Entity) {
                val tc = transformMapper.get(entity)
                world.tiledMap.setEntity(tc.pos.x.toInt(), tc.pos.y.toInt(), entity)
            }

            override fun entityRemoved(entity: Entity) {
                val tc = transformMapper.get(entity)
                world.tiledMap.removeEntity(tc.pos.x.toInt(), tc.pos.y.toInt(), entity)
            }
        })
    }


    override fun reset() {
        world.tiledMap.reset()
    }
}
