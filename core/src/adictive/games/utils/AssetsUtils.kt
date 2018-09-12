package adictive.games.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

object AssetsUtils {
    fun loadTexture(file: String): Texture {
        return Texture(Gdx.files.internal(file))
    }
}
