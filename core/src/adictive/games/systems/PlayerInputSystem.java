package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import adictive.games.components.MovementComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.utils.Families;


public class PlayerInputSystem extends EntitySystem implements Reseteable {
    private final Array<Entity> players = new Array<>();
    private final ComponentMapper<MovementComponent> movementMapper = ComponentMapper.getFor(MovementComponent.class);

    private final Vector3 touch = new Vector3(0, 0, 0);
    private final Vector3 touchOrigin = new Vector3(-1, -1, -1);

    @Override
    public void update(float deltaTime) {
        for (Entity player : players) {
            final MovementComponent movementComponent = movementMapper.get(player);

            if (Gdx.input.justTouched()) {
                touchOrigin.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
            }

            if (Gdx.input.isTouched() && touchOrigin.z != -1) {
                touch.set(Gdx.input.getX(), Gdx.input.getY(), 0f);

                touch.sub(touchOrigin.x, touchOrigin.y, 0).nor();

                movementComponent.accel.set(touch.x, -touch.y).nor().scl(PlayerComponent.ACCEL);
            } else {
                movementComponent.accel.set(0, 0);
            }
        }
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

    @Override
    public void reset() {
        touch.setZero();
        touchOrigin.set(-1,-1,-1);
        players.clear();
    }
}
