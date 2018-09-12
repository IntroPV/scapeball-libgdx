package adictive.games.utils

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

/**
 * Created by arielalvarez on 2/4/18.
 */

object ViewUtils {

    fun draw(batch: SpriteBatch, isTouched: Boolean, pos: Rectangle, texture: TextureRegion) {
        val scale = if (isTouched) 1.2f else 1f
        batch.draw(texture,
                pos.x - pos.width / 2 - if (isTouched) (scale - 1) * pos.width / 2f else 0f,
                pos.y - pos.height / 2 - if (isTouched) (scale - 1) * pos.width / 2f else 0f,
                0f, 0f,
                pos.width,
                pos.height,
                scale, scale, 0f
        )
    }
}
