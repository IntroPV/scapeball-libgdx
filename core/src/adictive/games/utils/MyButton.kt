package adictive.games.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

/**
 * Created by arielalvarez on 2/4/18.
 */

class MyButton(imgName: String, side: Int, x: Int, y: Int, private val action: () -> Unit) : MyView(TextureRegion(Texture(Gdx.files.internal("data/$imgName.png")))) {
    var isTouching: Boolean = false
        private set

    init {
        val ratio = this.texture.regionHeight.toFloat() / this.texture.regionWidth.toFloat()
        this.pos = Rectangle(x.toFloat(), y.toFloat(), side.toFloat(), side * ratio)
    }

    override fun render(batch: SpriteBatch) {
        val lastPositionTouchesButton = pos!!.contains(
                Gdx.input.x + pos!!.width / 2,
                Gdx.graphics.height - Gdx.input.y + pos!!.height / 2
        )

        val shouldAct = lastPositionTouchesButton && isTouching && !Gdx.input.isTouched

        isTouching = lastPositionTouchesButton && Gdx.input.justTouched() || isTouching && Gdx.input.isTouched

        ViewUtils.draw(batch, isTouching, pos!!, texture)

        if (shouldAct) {
            action.invoke()
        }
    }

}