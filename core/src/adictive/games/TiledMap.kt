package adictive.games

import adictive.games.components.*
import adictive.games.systems.Reseteable
import com.badlogic.ashley.core.Entity

class TiledMap : Reseteable {

    lateinit var tiles: Array<Array<Array<Entity?>>>

    fun init(width: Int, height: Int) {
        this.tiles = Array(width) { _ ->
            Array(height) { _ ->
                Array<Entity?>(REGISTRY.size) { null }
            }
        }
    }

    /*
     * O(len(REGISTRY))
     */
    fun setEntity(x: Int, y: Int, entity: Entity) {
        val entityConstant = getEntityConstant(entity)
        if (entityConstant >= 0) {
            tiles[x][y][entityConstant] = entity
        }
    }

    fun removeEntity(x: Int, y: Int, entity: Entity) {
        val entityConstant = getEntityConstant(entity)
        if (entityConstant >= 0) {
            tiles[x][y][entityConstant] = null
        }
    }

    fun getEntity(x: Int, y: Int, entityType: Int): Entity? {
        return this.tiles[x][y][entityType]
    }

    private fun getEntityConstant(entity: Entity): Int {
        val size = REGISTRY.size
        for (i in 0 until size) {
            if (entity.getComponent(REGISTRY[i]) != null) {
                return i
            }
        }
        return -1
    }

    override fun reset() {
        for (i in tiles.indices) {
            for (j in 0 until tiles[i].size) {
                for (k in 0 until MAX_ENTITIES_ON_TILE) {
                    tiles[i][j][k] = null
                }
            }
        }
    }

    companion object {
        const val MAX_ENTITIES_ON_TILE = 4

        const val WALL = 1
        const val WIN = 2
        const val COIN = 3
        const val SPIKE = 4
        const val HOLE = 5
        const val FLOOR = 6

        val REGISTRY = Array(7) {
            when (it) {
                WALL -> WallComponent::class.java
                WIN -> WinComponent::class.java
                COIN -> CoinComponent::class.java
                SPIKE -> SpikeComponent::class.java
                HOLE -> BlackHoleComponent::class.java
                FLOOR -> FloorEffectComponent::class.java
                else -> {
                    EmptyComponent::class.java
                }
            }
        }
    }
}
