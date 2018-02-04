package adictive.games.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by arielalvarez on 2/4/18.
 */

public class MyButton extends MyView {
    private OnButtonClick action;
    private boolean isTouching;

    public MyButton(String imgName, int side, int x, int y, OnButtonClick action) {
        super(new TextureRegion(new Texture(Gdx.files.internal("data/" + imgName + ".png"))));
        float ratio = (float) this.texture.getRegionHeight() / (float) this.texture.getRegionWidth();
        this.pos = new Rectangle(x, y, side, side * ratio);
        this.action = action;
    }

    public boolean isTouching() {
        return isTouching;
    }

    public void render(SpriteBatch batch) {
        boolean lastPositionTouchesButton = pos.contains(
                Gdx.input.getX() + pos.width / 2,
                Gdx.graphics.getHeight() - Gdx.input.getY() + pos.height / 2
        );

        boolean shouldAct = lastPositionTouchesButton && isTouching && !Gdx.input.isTouched() && action != null;

        isTouching = (lastPositionTouchesButton && Gdx.input.justTouched()) || (isTouching && Gdx.input.isTouched());

        ViewUtils.draw(batch, isTouching, pos, texture);

        if (shouldAct) {
            action.onClick();
        }
    }

}