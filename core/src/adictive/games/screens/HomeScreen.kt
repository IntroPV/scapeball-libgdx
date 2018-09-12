package adictive.games.screens

import adictive.games.MainGame
import adictive.games.utils.MyButton
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class HomeScreen(superCubito: MainGame) : ScreenAdapter() {
    private val icPlay: MyButton
    private val icShare: MyButton
    private val icSettings: MyButton
    private val icLevels: MyButton
    private val batch: SpriteBatch
    private var width: Int = 0
    private var height: Int = 0

    init {

        width = Gdx.graphics.width
        height = Gdx.graphics.height

        val centerX = width / 2
        val centerY = height / 2
        val zoneSide = Math.min(width, height) / 3

        icPlay = MyButton(
                "ic_play",
                zoneSide,
                centerX, centerY
        ) { superCubito.goToPlayScreen() }

        val smallButtonSide = zoneSide / 2

        icShare = MyButton(
                "ic_share",
                smallButtonSide,
                centerX + zoneSide, centerY - zoneSide
        ) { }

        icSettings = MyButton(
                "ic_gear",
                smallButtonSide,
                centerX, centerY - zoneSide
        ) { superCubito.goToSettingsScreen() }

        icLevels = MyButton(
                "ic_levels",
                smallButtonSide,
                centerX - zoneSide, centerY - zoneSide
        ) { }

        batch = SpriteBatch()

    }

    override fun render(delta: Float) {

        batch.begin()

        icPlay.render(batch)
        icSettings.render(batch)
        icLevels.render(batch)
        icShare.render(batch)

        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        this.width = Gdx.graphics.width
        this.height = Gdx.graphics.height
    }
}
