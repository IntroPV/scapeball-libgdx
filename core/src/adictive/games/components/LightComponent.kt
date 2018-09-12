package adictive.games.components

import adictive.games.utils.AssetsUtils.loadTexture
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion


class LightComponent : Component {
    var color = Color()
    var radius = 2f
    var shape = CIRCULAR

    companion object {
        val CIRCULAR = TextureRegion(loadTexture("data/light_circular.png"))
        val SQUARE = TextureRegion(loadTexture("data/light_square.png"))
    }
}
