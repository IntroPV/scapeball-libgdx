package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import adictive.games.components.MovementComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TransformComponent;

public class MovementSystem extends IteratingSystem implements Reseteable {
    private final Vector2 tmp = new Vector2();

    private final ComponentMapper<TransformComponent> tm;
    private final ComponentMapper<MovementComponent> mm;

    public MovementSystem() {
        super(Family.all(TransformComponent.class, MovementComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = tm.get(entity);
        MovementComponent movementComponent = mm.get(entity);

        transformComponent.lastPos.set(transformComponent.pos);

        movementComponent.velocity.add(movementComponent.accel);


        tmp.set(movementComponent.velocity);

        float velLen = movementComponent.velocity.len();
        if (velLen > PlayerComponent.MAX_VELOCITY) {
            tmp.nor().scl(PlayerComponent.MAX_VELOCITY);
            movementComponent.velocity.nor().scl(PlayerComponent.MAX_VELOCITY);
        }

        if (movementComponent.velocity.len() > movementComponent.friction) {
            //apply friction
            tmp.set(movementComponent.velocity).nor().scl(-movementComponent.friction);
            movementComponent.velocity.add(tmp);
        } else {
            movementComponent.velocity.set(0, 0);
        }

        tmp.set(movementComponent.velocity).scl(deltaTime);

        transformComponent.pos.add(tmp.x, tmp.y, 0.0f);

        if (movementComponent.velocity.len() > 0) {
            transformComponent.rotation = movementComponent.velocity.angle();
        }


    }

    @Override
    public void reset() {
        // Do nothing. Does not hold game state.
    }
}