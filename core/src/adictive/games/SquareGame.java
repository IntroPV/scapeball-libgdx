package adictive.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;

import adictive.games.analytics.IAnalyticsManager;
import adictive.games.play.PlayScreen;
import adictive.games.systems.Reseteable;
import adictive.games.utils.GameData;

public class SquareGame extends Game {
    private final FPSLogger logger;
    public final IAnalyticsManager analyticsManager;

    public SquareGame(IAnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
        logger = new FPSLogger();
    }

    @Override
    public void create() {
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0xFF/255f, 0xFC/255f, 0xEF/255f, 1.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        logger.log();
        super.render();
    }
}
