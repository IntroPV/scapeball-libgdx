package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class IceFloorComponent implements Component {
    public static final TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/iceFloor.png")));
    public static final float WIDTH = 1f;
    public static final float HEIGHT = 1f;

    public static Entity addNew(Engine engine, float x, float y) {
        Entity block = create((int) x, (int) y);
        engine.addEntity(block);
        return block;
    }

    public static Entity create(int x, int y) {
        Entity block = new Entity();

        TextureComponent textureComponent = new TextureComponent();

        textureComponent.region = textureRegion;
        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(WIDTH, HEIGHT);
        transformComponent.pos.set(x, y, 0f);
        block.add(transformComponent);

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0, 0, WIDTH, HEIGHT);
        block.add(boundsComponent);

        block.add(new IceFloorComponent());
        return block;
    }
}
