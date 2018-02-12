package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;

import adictive.games.components.PlayerComponent;
import adictive.games.components.TransformComponent;
import adictive.games.utils.Families;

/**
 * Created by arielalvarez on 2/4/18.
 */

public class PlayerEffectsSystem extends EntitySystem implements Reseteable {
    private final Array<Entity> players = new Array<>();
    private final ComponentMapper<PlayerComponent> playerComp = ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<TransformComponent> tc = ComponentMapper.getFor(TransformComponent.class);

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
    public void update(float deltaTime) {
        for (Entity entity : players) {
            PlayerComponent player = playerComp.get(entity);
            TransformComponent body = tc.get(entity);

            player.trailX[4] = player.trailX[3];
            player.trailX[3] = player.trailX[2];
            player.trailX[2] = player.trailX[1];
            player.trailX[1] = player.trailX[0];
            player.trailX[0] = body.pos.x;

            player.trailY[4] = player.trailY[3];
            player.trailY[3] = player.trailY[2];
            player.trailY[2] = player.trailY[1];
            player.trailY[1] = player.trailY[0];
            player.trailY[0] = body.pos.y;
        }
    }

    @Override
    public void reset() {
        players.clear();
    }
}
