package adictive.games.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by arielalvarez on 2/4/18.
 */

public class MyView {
    public final TextureRegion texture;
    public Rectangle pos;

    public MyView(TextureRegion texture) {
        this.texture = texture;
    }

    public void render(SpriteBatch batch) {
        ViewUtils.draw(batch, false, pos, texture);
    }
}
