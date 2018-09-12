package adictive.games.screens

import adictive.games.MainGame
import adictive.games.utils.GameData
import adictive.games.utils.MyButton
import adictive.games.utils.MyView
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class SettingsScreen(superCubito: MainGame) : ScreenAdapter() {
    private val icEnter: MyButton
    private val selectControlsLeft: MyButton
    private val selectControlsRight: MyButton
    private val selectedBorder: MyView
    private val batch: SpriteBatch
    private var joystickPosition: Int = 0
    private var width: Int = 0
    private var height: Int = 0

    init {

        width = Gdx.graphics.width
        height = Gdx.graphics.height

        joystickPosition = GameData.joystickPosition

        val zoneSide = Math.min(width, height) / 5

        icEnter = MyButton(
                "ic_enter",
                zoneSide,
                width - (zoneSide * 0.75f).toInt(), (zoneSide * 0.75f).toInt()
        ) { superCubito.goToHomeScreen() }

        selectControlsLeft = MyButton(
                "settings_left_joystick",
                (width / 2f * 0.9f).toInt(),
                width / 4, height / 2 + (zoneSide * 0.75f).toInt()
        ) {
            GameData.joystickPosition = 1
            joystickPosition = 1
        }


        selectControlsRight = MyButton(
                "settings_right_joystick",
                (width / 2f * 0.9f).toInt(),
                width / 4 * 3, height / 2 + (zoneSide * 0.75f).toInt()
        ) {
            GameData.joystickPosition = 0
            joystickPosition = 0
        }

        selectedBorder = MyView(TextureRegion(Texture(Gdx.files.internal("data/settings_selected_joystick.png"))))

        batch = SpriteBatch()

    }

    override fun render(delta: Float) {

        batch.begin()

        selectControlsLeft.render(batch)
        selectControlsRight.render(batch)

        if (joystickPosition == 0) {
            selectedBorder.pos = selectControlsRight.pos
        } else {
            selectedBorder.pos = selectControlsLeft.pos
        }

        if (!selectControlsRight.isTouching && !selectControlsLeft.isTouching) {

            selectedBorder.render(batch)
        }

        icEnter.render(batch)

        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        this.width = Gdx.graphics.width
        this.height = Gdx.graphics.height
    }
}
