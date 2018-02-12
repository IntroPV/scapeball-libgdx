package adictive.games;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class SquareWorld {
    final int width = 32;
    final int height = 32;

    private OrthographicCamera camera;
    public final TiledMap tiledMap = new TiledMap();

    public SquareWorld() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        new FillViewport(width, height, camera);
        tiledMap.init(width, height);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
