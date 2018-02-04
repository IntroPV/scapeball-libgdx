package adictive.games.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by arielalvarez on 2/4/18.
 */

public class ViewUtils {

    public static void draw(SpriteBatch batch, boolean isTouched, Rectangle pos, TextureRegion texture) {
        float scale = isTouched ? 1.2f : 1f;
        batch.draw(texture,
                pos.x - pos.width / 2 - (isTouched ? ((scale - 1) * pos.width) / 2 : 0),
                pos.y - pos.height / 2 - (isTouched ? ((scale - 1) * pos.width) / 2 : 0),
                0, 0,
                pos.width,
                pos.height,
                scale, scale, 0f
        );
    }
}
