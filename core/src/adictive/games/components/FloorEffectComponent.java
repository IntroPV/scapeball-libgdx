package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FloorEffectComponent implements Component {
    public static final TextureRegion iceRegion = new TextureRegion(new Texture(Gdx.files.internal("data/iceFloor.png")));
    public static final TextureRegion dirtRegion = new TextureRegion(new Texture(Gdx.files.internal("data/dirtFloor.png")));
    public static final TextureRegion accelRegion = new TextureRegion(new Texture(Gdx.files.internal("data/floorAccel.png")));
    public static final float WIDTH = 1f;
    public static final float HEIGHT = 1f;

    public static final byte ICE = 1;
    public static final byte DIRT = 2;
    public static final byte ACCELERATOR = 3;

    public static Entity addNew(Engine engine, float x, float y, byte type) {
        Entity block = create((int) x, (int) y, type);
        engine.addEntity(block);
        return block;
    }

    public static Entity create(int x, int y, byte type) {
        Entity block = new Entity();
        block.flags = type;

        TextureComponent textureComponent = new TextureComponent();

        switch (type) {
            case ICE:
                textureComponent.region = iceRegion;
                break;
            case DIRT:
                textureComponent.region = dirtRegion;
                break;
            case ACCELERATOR:
                textureComponent.region = accelRegion;
                break;
        }

        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(WIDTH, HEIGHT);
        transformComponent.pos.set(x, y, 0f);
        block.add(transformComponent);

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0, 0, WIDTH, HEIGHT);
        block.add(boundsComponent);

        block.add(new FloorEffectComponent());
        return block;
    }
}
