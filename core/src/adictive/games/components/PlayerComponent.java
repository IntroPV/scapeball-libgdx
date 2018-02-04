package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import adictive.games.SquareWorld;

public class PlayerComponent implements Component {
    public static final float MAX_VELOCITY = 8;
    public static final float ACCEL = 2;
    public static final float WIDTH = 0.5f;
    public static final float HEIGHT = 0.5f;
    public static final float LAYER = 3;
    public static final Texture playerTexture = new Texture(Gdx.files.internal("data/player_sheet.png"));

    private static final TextureRegion textureRegion = new TextureRegion(playerTexture, 0, 0, 24, 24);

    public static final TextureRegion trail1 = new TextureRegion(playerTexture, 24, 0, 24, 24);
    public static final TextureRegion trail2 = new TextureRegion(playerTexture, 24 * 2, 0, 24, 24);
    public static final TextureRegion trail3 = new TextureRegion(playerTexture, 24 * 3, 0, 24, 24);
    public static final TextureRegion trail4 = new TextureRegion(playerTexture, 24 * 4, 0, 24, 24);

    public final float[] trailX = new float[5];
    public final float[] trailY = new float[5];

    public static void addNew(SquareWorld world, Engine engine, float x, float y) {
        Entity player = create(world, x, y);
        engine.addEntity(player);
    }

    public static Entity create(SquareWorld world, float x, float y) {
        Entity player = new Entity();

        player.add(new PlayerComponent());

        CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.camera = world.getCamera();
        player.add(cameraComponent);

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = textureRegion;
//        textureRegion.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        player.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(WIDTH, HEIGHT);
        transformComponent.pos.set(x,y, LAYER);
        player.add(transformComponent);

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0,0,0.5f,0.5f);
        player.add(boundsComponent);

        LightComponent lightComponent = new LightComponent();
        lightComponent.color = new Color(1f,0,0,0.4f);
        lightComponent.radius = 1.5f;
        player.add(lightComponent);

        player.add(new MovementComponent());
        player.add(new AttractionComponent());
        return player;
    }
}
