package adictive.games

import adictive.games.analytics.IAnalyticsManager
import adictive.games.screens.HomeScreen
import adictive.games.screens.PlayScreen
import adictive.games.screens.SettingsScreen
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20

class MainGame(val analyticsManager: IAnalyticsManager) : Game() {

    private val logger: FPSLogger = FPSLogger()
    private var playScreen: Screen? = null
    private var homeScreen: Screen? = null
    private var settingsScreen: Screen? = null

    override fun create() {
        if (playScreen == null) {
            playScreen = PlayScreen(this)
        }

        if (homeScreen == null) {
            homeScreen = HomeScreen(this)
        }

        if (settingsScreen == null) {
            settingsScreen = SettingsScreen(this)
        }

        setScreen(homeScreen)
    }


    override fun render() {
        val gl = Gdx.gl
        gl.glClearColor(0xFF / 255f, 0xFC / 255f, 0xEF / 255f, 1.0f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        logger.log()
        super.render()
    }

    fun goToPlayScreen() {
        setScreen(playScreen)
    }

    fun goToHomeScreen() {
        setScreen(homeScreen)
    }

    fun goToSettingsScreen() {
        setScreen(settingsScreen)
    }
}
