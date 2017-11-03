package adictive.games;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SquareWorld {
    final int width = 64;
    final int height = 64;

    private Viewport viewport;
    private OrthographicCamera camera;

    public SquareWorld() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new FillViewport(width, height, camera);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
