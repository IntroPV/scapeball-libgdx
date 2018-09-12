package adictive.games.utils

import com.badlogic.gdx.Gdx

object GameData {
    private val PREFS_NAME = "GameData"
    private val DATA_LEVEL = "level"

    val JOYSTICK_POSITION = "joystickPosition"

    val currentLevel: Int
        get() {

            val prefs = Gdx.app.getPreferences(PREFS_NAME)
            return prefs.getInteger(DATA_LEVEL, 1)
        }

    var joystickPosition: Int
        get() = Gdx.app.getPreferences(PREFS_NAME).getInteger(JOYSTICK_POSITION, 0)
        set(position) {
            val prefs = Gdx.app.getPreferences(PREFS_NAME)
            prefs.putInteger(JOYSTICK_POSITION, position)
            prefs.flush()
        }

    fun incrementCurrentLevel(): Int {
        val prefs = Gdx.app.getPreferences(PREFS_NAME)
        val level = prefs.getInteger(DATA_LEVEL, 1) + 1
        prefs.putInteger(DATA_LEVEL, level)
        prefs.flush()
        return level
    }

    fun decrementCurrentLevel(): Int {
        val prefs = Gdx.app.getPreferences(PREFS_NAME)
        val level = Math.max(prefs.getInteger(DATA_LEVEL, 1) - 1, 1)
        prefs.putInteger(DATA_LEVEL, level)
        prefs.flush()
        return level
    }
}
