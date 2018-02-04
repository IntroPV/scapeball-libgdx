package adictive.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;

import adictive.games.analytics.IAnalyticsManager;
import adictive.games.screens.HomeScreen;
import adictive.games.screens.PlayScreen;
import adictive.games.screens.SettingsScreen;

public class MainGame extends Game {
    private final FPSLogger logger;
    public final IAnalyticsManager analyticsManager;

    private Screen playScreen;
    private Screen homeScreen;
    private Screen settingsScreen;

    public MainGame(IAnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
        logger = new FPSLogger();
    }

    @Override
    public void create() {
        if (playScreen == null) {
            playScreen = new PlayScreen(this);
        }

        if (homeScreen == null) {
            homeScreen = new HomeScreen(this);
        }

        if (settingsScreen == null) {
            settingsScreen = new SettingsScreen(this);
        }

        setScreen(homeScreen);
    }


    @Override
    public void render() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0xFF/255f, 0xFC/255f, 0xEF/255f, 1.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        logger.log();
        super.render();
    }

    public void goToPlayScreen() {
        setScreen(playScreen);
    }

    public void goToHomeScreen() {
        setScreen(homeScreen);
    }

    public void goToSettingsScreen() {
        setScreen(settingsScreen);
    }
}
