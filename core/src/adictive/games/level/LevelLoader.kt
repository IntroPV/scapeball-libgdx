package adictive.games.level

import adictive.games.SquareWorld
import adictive.games.components.*
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx

class LevelLoader(private val world: SquareWorld, private val engine: Engine) {

    fun load(levelNumber: Int) {
        val fileName = "level$levelNumber.txt"
        val fileHandle = Gdx.files.internal("levels/$fileName")
        val s = fileHandle.readString()
        val lines = s.split("\n".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        for (line in lines) {
            val entity = line.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            when (entity[0]) {
                "Block" -> parseBlock(entity)
                "Enemy" -> parseEnemy(entity)
                "Player" -> parsePlayer(entity)
                "Win" -> parseWin(entity)
                "Floor" -> parseFloor(entity)
                "Coin" -> parseCoin(entity)
                "Spike" -> parseSpike(entity)
                "Hole" -> parseHole(entity)
            }
        }
    }

    private fun parseHole(line: Array<String>) {
        BlackHoleComponent.addNew(
                engine,
                java.lang.Float.parseFloat(line[1]), //x
                java.lang.Float.parseFloat(line[2]), //y
                java.lang.Float.parseFloat(line[3])       //attraction
        )
    }

    private fun parseSpike(line: Array<String>) {
        SpikeComponent.addNew(
                engine,
                java.lang.Float.parseFloat(line[1]).toInt(), //x
                java.lang.Float.parseFloat(line[2]).toInt(), //y
                java.lang.Float.parseFloat(line[3])       //rotation
        )
    }

    private fun parseCoin(line: Array<String>) {
        CoinComponent.addNew(
                engine,
                java.lang.Float.parseFloat(line[1]), java.lang.Float.parseFloat(line[2])
        )
    }

    private fun parseWin(line: Array<String>) {
        WinComponent.addNew(
                engine,
                java.lang.Float.parseFloat(line[1]), java.lang.Float.parseFloat(line[2])
        )
    }

    private fun parseFloor(line: Array<String>) {
        FloorEffectComponent.addNew(
                engine,
                java.lang.Float.parseFloat(line[1]), java.lang.Float.parseFloat(line[2]), java.lang.Byte.parseByte(line[3])
        )
    }

    private fun parsePlayer(line: Array<String>) {
        PlayerComponent.addNew(
                world, engine,
                java.lang.Float.parseFloat(line[1]), java.lang.Float.parseFloat(line[2])
        )
    }

    private fun parseEnemy(line: Array<String>) {
        EnemyComponent.addNew(
                engine, 0f, 0f,
                java.lang.Float.parseFloat(line[1]),
                java.lang.Float.parseFloat(line[2]),
                java.lang.Float.parseFloat(line[3]),
                java.lang.Float.parseFloat(line[4]),
                java.lang.Float.parseFloat(line[5])
        )
    }

    private fun parseBlock(line: Array<String>) {
        loadBlock(world, engine, Integer.parseInt(line[1]), Integer.parseInt(line[2]), if (line.size >= 4) Integer.parseInt(line[3]) else 0)
    }

    private fun loadBlock(world: SquareWorld, engine: Engine, x: Int, y: Int, flags: Int) {
        WallComponent.addNewWall(engine, x, y, flags)
    }
}
