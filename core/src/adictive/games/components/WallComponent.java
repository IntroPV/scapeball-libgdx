package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WallComponent implements Component {

    private static final TextureRegion blackTextureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/blackBox.png")));
    private static final TextureRegion chainTextureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/chainBox.png")));

    public static final int FLAG_CHAINBOX = 0x1;

    public static Entity addNewWall(Engine engine, int x, int y, int flag) {
        Entity block = create(x, y, flag);

        engine.addEntity(block);
        return block;
    }

    public static Entity create(int x, int y, int flag) {
        Entity block = new Entity();
        block.flags = flag;

        final TextureComponent textureComponent = new TextureComponent();

        if ((flag & FLAG_CHAINBOX) == FLAG_CHAINBOX) {
            textureComponent.region = chainTextureRegion;
        } else {
            textureComponent.region = blackTextureRegion;
        }

        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(1f, 1f);
        transformComponent.pos.set(x,y,0f);
        block.add(transformComponent);

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0,0,1f,1f);
        block.add(boundsComponent);

        block.add(new WallComponent());
        return block;
    }
}
