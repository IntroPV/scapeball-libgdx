package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;

import adictive.games.SquareWorld;
import adictive.games.TiledMap;
import adictive.games.components.MovementComponent;
import adictive.games.components.TransformComponent;
import adictive.games.utils.Families;


public class FloorEffectSystem extends EntitySystem implements Reseteable {
    private final Array<Entity> players = new Array<>();
    private final ComponentMapper<TransformComponent> tm;
    private final ComponentMapper<MovementComponent> mm;
    private SquareWorld world;

    private static final float ICE_FRICTION = 0f;
    private static final float NORMAL_FRICTION = 0.2f;

    public FloorEffectSystem(SquareWorld world) {
        this.world = world;
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        for (Entity player : players) {
            MovementComponent mc = mm.get(player);
            TransformComponent tc = tm.get(player);

            final int x = (int) (tc.pos.x + tc.size.x / 2);
            final int y = (int) (tc.pos.y + tc.size.y / 2);

            Entity iceFloor = world.tiledMap.getEntity(x, y, TiledMap.ICE);

            if (iceFloor != null) {
                mc.friction = ICE_FRICTION;
            } else {
                mc.friction = NORMAL_FRICTION;
            }
        }
    }

    @Override
    public void reset() {
        players.clear();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(
                Families.PLAYER,
                new EntityListener() {
                    @Override
                    public void entityAdded(Entity entity) {
                        players.add(entity);
                    }

                    @Override
                    public void entityRemoved(Entity entity) {
                        players.removeValue(entity, true);
                    }
                }
        );
    }
}