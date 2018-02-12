package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;

import adictive.games.SquareWorld;
import adictive.games.components.TransformComponent;
import adictive.games.utils.Families;

public class TileUpdateSystem extends EntitySystem implements Reseteable {

    private final SquareWorld world;

    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);

    public TileUpdateSystem(SquareWorld world) {
        super();
        this.world = world;
    }

    @Override
    public void addedToEngine(final Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(Families.BOUNDS, new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                final TransformComponent tc = transformMapper.get(entity);
                world.tiledMap.setEntity((int) tc.pos.x, (int) tc.pos.y, entity);
            }

            @Override
            public void entityRemoved(Entity entity) {
                final TransformComponent tc = transformMapper.get(entity);
                world.tiledMap.removeEntity((int) tc.pos.x, (int) tc.pos.y, entity);
            }
        });
    }


    @Override
    public void reset() {
        world.tiledMap.reset();
    }
}
