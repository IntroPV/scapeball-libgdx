package adictive.games.systems

import adictive.games.SquareWorld
import adictive.games.TiledMap
import adictive.games.components.BoundsComponent
import adictive.games.components.EnemyComponent
import adictive.games.components.PlayerComponent
import adictive.games.components.TransformComponent
import adictive.games.screens.PlayScreen
import adictive.games.utils.Families
import com.badlogic.ashley.core.*
import java.util.*

class CollisionSystem(private val world: SquareWorld, private val screen: PlayScreen) : EntitySystem(), Reseteable {

    private val transformMapper = ComponentMapper.getFor<TransformComponent>(TransformComponent::class.java)
    private val boundsMapper = ComponentMapper.getFor<BoundsComponent>(BoundsComponent::class.java)
    private val enemies = ArrayList<Entity>()
    private var player: Entity? = null
    private var playerTr: TransformComponent? = null
    private var playerBc: BoundsComponent? = null

    override fun reset() {
        enemies.clear()
        player = null
        playerTr = null
        playerBc = null
    }

    override fun update(deltaTime: Float) {
        checkWallCollisionAndRespond()
        checkEnemyCollision()
        checkRectangleCollisionInsideCell(TiledMap.SPIKE)
        checkBlackHoleCollision()
        checkWinBlockCollision()
        checkCoinCollision()
    }

    private fun checkBlackHoleCollision() {
        val x = (playerTr!!.pos.x + playerBc!!.bounds.x / 2).toInt()
        val y = (playerTr!!.pos.y + playerBc!!.bounds.y / 2).toInt()
        val hole = world.tiledMap.getEntity(x, y, TiledMap.HOLE)
        if (hole != null) {
            screen.killed()
        }
    }

    private fun checkCoinCollision() {
        val x = (playerTr!!.pos.x + playerBc!!.bounds.x / 2).toInt()
        val y = (playerTr!!.pos.y + playerBc!!.bounds.y / 2).toInt()
        val coin = world.tiledMap.getEntity(x, y, TiledMap.COIN)
        if (coin != null && playerOverlaps(coin.getComponent(TransformComponent::class.java), coin.getComponent(BoundsComponent::class.java))) {
            engine.removeEntity(coin)
        }
    }

    private fun checkWinBlockCollision() {
        val winEntity = world.tiledMap.getEntity((playerTr!!.pos.x + playerBc!!.bounds.x / 2).toInt(), (playerTr!!.pos.y + playerBc!!.bounds.y / 2).toInt(), TiledMap.WIN)
        if (winEntity != null) {
            screen.win()
        }
    }

    private fun checkEnemyCollision() {
        for (enemy in enemies) {
            val enemyTr = transformMapper.get(enemy)
            val enemyBc = boundsMapper.get(enemy)

            if (playerOverlaps(enemyTr, enemyBc)) {
                screen.killed()
                break
            }
        }
    }

    private fun checkRectangleCollisionInsideCell(type: Int) {
        checkPointAgainstRectangleInsideCellCollision(playerTr!!.pos.x.toInt(), playerTr!!.pos.y.toInt(), type)
        checkPointAgainstRectangleInsideCellCollision((playerTr!!.pos.x + playerBc!!.bounds.width).toInt(), playerTr!!.pos.y.toInt(), type)
        checkPointAgainstRectangleInsideCellCollision(playerTr!!.pos.x.toInt(), (playerTr!!.pos.y + playerBc!!.bounds.height).toInt(), type)
        checkPointAgainstRectangleInsideCellCollision((playerTr!!.pos.x + playerBc!!.bounds.width).toInt(), (playerTr!!.pos.y + playerBc!!.bounds.height).toInt(), type)
    }

    private fun checkPointAgainstRectangleInsideCellCollision(x: Int, y: Int, type: Int) {
        val spike = world.tiledMap.getEntity(x, y, type)
        if (spike != null && playerOverlaps(spike.getComponent(TransformComponent::class.java), spike.getComponent(BoundsComponent::class.java))) {
            screen.killed()
        }
    }

    private fun playerOverlaps(enemyTr: TransformComponent, enemyBc: BoundsComponent): Boolean {
        val enemyX = enemyTr.pos.x + enemyBc.bounds.x
        val enemyY = enemyTr.pos.y + enemyBc.bounds.y
        return (playerTr!!.pos.x < enemyX + enemyBc.bounds.width
                && playerTr!!.pos.x + playerBc!!.bounds.width > enemyX
                && playerTr!!.pos.y < enemyY + enemyBc.bounds.height
                && playerTr!!.pos.y + playerBc!!.bounds.height > enemyY)
    }

    private fun checkWallCollisionAndRespond() {
        val playerTr = transformMapper.get(player!!)
        val playerBc = boundsMapper.get(player!!)

        checkVertexAndRespond(playerTr, playerBc, 0f, 0f)
        checkVertexAndRespond(playerTr, playerBc, playerBc.bounds.width, 0f)
        checkVertexAndRespond(playerTr, playerBc, 0f, playerBc.bounds.height)
        checkVertexAndRespond(playerTr, playerBc, playerBc.bounds.width, playerBc.bounds.height)

    }

    private fun checkVertexAndRespond(playerTr: TransformComponent, playerBc: BoundsComponent, deltaX: Float, deltaY: Float) {
        val changeX = checkCollisionAxisX(playerTr, playerBc, deltaX, deltaY)
        val changeY = checkCollisionAxisY(playerTr, playerBc, deltaX, deltaY)

        if (Math.abs(changeX - playerTr.lastPos.x) < Math.abs(changeY - playerTr.lastPos.y)) {
            playerTr.pos.x = changeX
            playerTr.pos.y = checkCollisionAxisY(playerTr, playerBc, deltaX, deltaY)
        } else {
            playerTr.pos.y = changeY
            playerTr.pos.x = checkCollisionAxisX(playerTr, playerBc, deltaX, deltaY)
        }
    }

    private fun checkCollisionAxisX(playerTr: TransformComponent, playerBc: BoundsComponent, deltaX: Float, deltaY: Float): Float {
        val cellX = (playerTr.pos.x + deltaX).toInt()
        val cellY = (playerTr.pos.y + deltaY).toInt()

        if (world.tiledMap.getEntity(cellX, cellY, TiledMap.WALL) != null && (playerTr.lastPos.x + deltaX).toInt() != cellX) {
            if (playerTr.lastPos.x < playerTr.pos.x) {
                return cellX.toFloat() - playerBc.bounds.width - PADDING
            } else if (playerTr.lastPos.x > playerTr.pos.x) {
                return cellX.toFloat() + 1f + PADDING
            }
        }
        return playerTr.pos.x
    }

    private fun checkCollisionAxisY(playerTr: TransformComponent, playerBc: BoundsComponent, deltaX: Float, deltaY: Float): Float {
        val cellX = (playerTr.pos.x + deltaX).toInt()
        val cellY = (playerTr.pos.y + deltaY).toInt()

        if (world.tiledMap.getEntity(cellX, cellY, TiledMap.WALL) != null && (playerTr.lastPos.y + deltaY).toInt() != cellY) {
            if (playerTr.lastPos.y < playerTr.pos.y) {
                return cellY.toFloat() - playerBc.bounds.height - PADDING
            } else if (playerTr.lastPos.y > playerTr.pos.y) {
                return cellY.toFloat() + 1f + PADDING
            }
        }
        return playerTr.pos.y
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        engine!!.addEntityListener(Families.BOUNDS, object : EntityListener {
            override fun entityAdded(entity: Entity) {
                val tc = transformMapper.get(entity)

                if (entity.getComponent<EnemyComponent>(EnemyComponent::class.java) != null) {
                    enemies.add(entity)
                } else if (entity.getComponent<PlayerComponent>(PlayerComponent::class.java) != null) {
                    player = entity
                    playerTr = tc
                    playerBc = boundsMapper.get(player!!)
                }
            }

            override fun entityRemoved(entity: Entity) {
                val tc = transformMapper.get(entity)
                if (entity.getComponent<EnemyComponent>(EnemyComponent::class.java) != null) {
                    enemies.remove(entity)
                } else if (entity.getComponent<PlayerComponent>(PlayerComponent::class.java) != null) {
                    player = null
                }
            }
        })
    }

    companion object {

        const val PADDING = 0.01f
    }
}
