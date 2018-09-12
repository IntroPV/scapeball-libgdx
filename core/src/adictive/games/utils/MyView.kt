package adictive.games.utils

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

/**
 * Created by arielalvarez on 2/4/18.
 */

open class MyView(val texture: TextureRegion) {
    var pos: Rectangle? = null

    open fun render(batch: SpriteBatch) {
        ViewUtils.draw(batch, false, pos!!, texture)
    }
}
