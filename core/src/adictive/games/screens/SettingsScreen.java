package adictive.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import adictive.games.MainGame;
import adictive.games.utils.GameData;
import adictive.games.utils.MyButton;
import adictive.games.utils.MyView;

public class SettingsScreen extends ScreenAdapter {
    private final MyButton icEnter;
    private final MyButton selectControlsLeft;
    private final MyButton selectControlsRight;
    private final MyView selectedBorder;
    private final SpriteBatch batch;
    private int joystickPosition;
    private int width;
    private int height;

    public SettingsScreen(MainGame superCubito) {

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        joystickPosition = GameData.getJoystickPosition();

        int zoneSide = (Math.min(width, height)) / 5;

        icEnter = new MyButton(
                "ic_enter",
                zoneSide,
                width - (int) (zoneSide * 0.75f), (int) (zoneSide * 0.75f),
                superCubito::goToHomeScreen
        );

        selectControlsLeft = new MyButton(
                "settings_left_joystick",
                (int) ((width / 2f) * 0.9f),
                width / 4, (height / 2) + (int) (zoneSide * 0.75f),
                () -> {
                    GameData.setJoystickPosition(1);
                    joystickPosition = 1;
                }
        );


        selectControlsRight = new MyButton(
                "settings_right_joystick",
                (int) ((width / 2f) * 0.9f),
                (width / 4) * 3, (height / 2) + (int) (zoneSide * 0.75f),
                () -> {
                    GameData.setJoystickPosition(0);
                    joystickPosition = 0;
                }
        );

        selectedBorder = new MyView(new TextureRegion(new Texture(Gdx.files.internal("data/settings_selected_joystick.png"))));

        batch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {

        batch.begin();

        selectControlsLeft.render(batch);
        selectControlsRight.render(batch);

        if (joystickPosition == 0) {
            selectedBorder.pos = selectControlsRight.pos;
        } else {
            selectedBorder.pos = selectControlsLeft.pos;
        }

        if (!selectControlsRight.isTouching() && !selectControlsLeft.isTouching()) {

            selectedBorder.render(batch);
        }

        icEnter.render(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
    }
}
