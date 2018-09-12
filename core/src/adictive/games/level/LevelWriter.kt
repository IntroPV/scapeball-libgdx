package adictive.games.level

import adictive.games.components.*
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.io.UnsupportedEncodingException

class LevelWriter {

    fun write(engine: Engine, level: Int) {
        val writer: PrintWriter
        try {
            val fileName = "level$level.txt"
            writer = PrintWriter("levels/$fileName", "UTF-8")
            val entities = engine.entities
            for (e in entities) {
                entityToCSV(writer, e)
            }
            writer.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }

    private fun entityToCSV(writer: PrintWriter, e: Entity) {
        if (e.getComponent<WallComponent>(WallComponent::class.java) != null) {
            writer.println(blockToCSV(e))
        } else if (e.getComponent<EnemyComponent>(EnemyComponent::class.java) != null) {
            writer.println(enemyToCSV(e))
        } else if (e.getComponent<PlayerComponent>(PlayerComponent::class.java) != null) {
            writer.println(playerToCSV(e))
        } else if (e.getComponent<WinComponent>(WinComponent::class.java) != null) {
            writer.println(winBlockToCSV(e))
        } else if (e.getComponent<CoinComponent>(CoinComponent::class.java) != null) {
            writer.println(coinToCSV(e))
        } else if (e.getComponent<BlackHoleComponent>(BlackHoleComponent::class.java) != null) {
            writer.println(holeToCSV(e))
        } else if (e.getComponent<SpikeComponent>(SpikeComponent::class.java) != null) {
            writer.println(spikeToCSV(e))
        } else if (e.getComponent<FloorEffectComponent>(FloorEffectComponent::class.java) != null) {
            writer.println(iceBlockToCSV(e))
        }
    }

    private fun spikeToCSV(e: Entity): String {
        val tc = e.getComponent<TransformComponent>(TransformComponent::class.java)
        return csv("Spike", tc.pos.x, tc.pos.y, tc.rotation)
    }

    private fun coinToCSV(e: Entity): String {
        val tc = e.getComponent<TransformComponent>(TransformComponent::class.java)
        return csv("Coin", tc.pos.x, tc.pos.y)
    }

    private fun winBlockToCSV(e: Entity): String {
        val tc = e.getComponent<TransformComponent>(TransformComponent::class.java)
        return csv("Win", tc.pos.x, tc.pos.y)
    }

    private fun iceBlockToCSV(e: Entity): String {
        val tc = e.getComponent<TransformComponent>(TransformComponent::class.java)
        return csv("Floor", tc.pos.x, tc.pos.y, e.flags)
    }

    fun playerToCSV(e: Entity): String {
        val tc = e.getComponent<TransformComponent>(TransformComponent::class.java)
        return csv("Player", tc.pos.x, tc.pos.y)
    }

    fun enemyToCSV(e: Entity): String {
        val ec = e.getComponent<EnemyComponent>(EnemyComponent::class.java)
        return csv("Enemy", ec.initialPosInLine, ec.start.x, ec.start.y, ec.end.x, ec.end.y)
    }

    fun blockToCSV(e: Entity): String {
        val tc = e.getComponent<TransformComponent>(TransformComponent::class.java)
        return csv("Block", tc.pos.x.toInt(), tc.pos.y.toInt(), e.flags)
    }

    fun holeToCSV(e: Entity): String {
        val tc = e.getComponent<TransformComponent>(TransformComponent::class.java)
        val bhc = e.getComponent<BlackHoleComponent>(BlackHoleComponent::class.java)
        return csv("Hole", tc.pos.x, tc.pos.y, bhc.attraction)
    }

    private fun csv(vararg e: Any): String {
        val sb = StringBuilder()
        for (s in e) {
            sb.append(s.toString())
            sb.append(",")
        }
        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }
}
