package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;

import adictive.games.components.MovementComponent;
import adictive.games.components.TransformComponent;
import adictive.games.utils.Families;


public class FloorEffectSystem extends EntitySystem implements Reseteable {
    private final Array<Entity> players = new Array<>();
    private final ComponentMapper<TransformComponent> tm;
    private final ComponentMapper<MovementComponent> mm;

    public FloorEffectSystem() {
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void update(float deltaTime) {

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