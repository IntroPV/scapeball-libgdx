package adictive.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import adictive.games.MainGame;
import adictive.games.utils.MyButton;

public class HomeScreen extends ScreenAdapter {
    private final MyButton icPlay;
    private final MyButton icShare;
    private final MyButton icSettings;
    private final MyButton icLevels;
    private final SpriteBatch batch;
    private int width;
    private int height;

    public HomeScreen(MainGame superCubito) {

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        int centerX = width / 2;
        int centerY = height / 2;
        int zoneSide = (Math.min(width, height)) / 3;

        icPlay = new MyButton(
                "ic_play",
                zoneSide,
                centerX, centerY,
                superCubito::goToPlayScreen
        );

        int smallButtonSide = zoneSide / 2;

        icShare = new MyButton(
                "ic_share",
                smallButtonSide,
                centerX + zoneSide, centerY - zoneSide,
                () -> {
                }
        );

        icSettings = new MyButton(
                "ic_gear",
                smallButtonSide,
                centerX, centerY - zoneSide,
                superCubito::goToSettingsScreen
        );

        icLevels = new MyButton(
                "ic_levels",
                smallButtonSide,
                centerX - zoneSide, centerY - zoneSide,
                () -> {
                }
        );

        batch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {

        batch.begin();

        icPlay.render(batch);
        icSettings.render(batch);
        icLevels.render(batch);
        icShare.render(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
    }
}
