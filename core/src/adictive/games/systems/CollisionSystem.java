package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;

import java.util.ArrayList;
import java.util.List;

import adictive.games.SquareWorld;
import adictive.games.TiledMap;
import adictive.games.components.BoundsComponent;
import adictive.games.components.EnemyComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TransformComponent;
import adictive.games.screens.PlayScreen;
import adictive.games.utils.Families;

public class CollisionSystem extends EntitySystem implements Reseteable {

    private static final float PADDING = 0.01f;

    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<BoundsComponent> boundsMapper = ComponentMapper.getFor(BoundsComponent.class);

    private final SquareWorld world;
    private PlayScreen screen;
    private final List<Entity> enemies = new ArrayList<>();
    private Entity player;
    private TransformComponent playerTr;
    private BoundsComponent playerBc;

    public CollisionSystem(SquareWorld world, PlayScreen screen) {
        super();
        this.world = world;
        this.screen = screen;
    }

    @Override
    public void reset() {
        enemies.clear();
        player = null;
        playerTr = null;
        playerBc = null;
    }

    @Override
    public void update(float deltaTime) {
        checkWallCollisionAndRespond();
        checkEnemyCollision();
        checkRectangleCollisionInsideCell(TiledMap.SPIKE);
        checkBlackHoleCollision();
        checkWinBlockCollision();
        checkCoinCollision();
    }

    private void checkBlackHoleCollision() {
        final int x = (int) (playerTr.pos.x + playerBc.bounds.x / 2);
        final int y = (int) (playerTr.pos.y + playerBc.bounds.y / 2);
        final Entity hole = world.tiledMap.getEntity(x, y, TiledMap.HOLE);
        if (hole != null) {
            screen.killed();
        }
    }

    private void checkCoinCollision() {
        final int x = (int) (playerTr.pos.x + playerBc.bounds.x / 2);
        final int y = (int) (playerTr.pos.y + playerBc.bounds.y / 2);
        final Entity coin = world.tiledMap.getEntity(x, y, TiledMap.COIN);
        if (coin != null && playerOverlaps(coin.getComponent(TransformComponent.class), coin.getComponent(BoundsComponent.class))) {
            getEngine().removeEntity(coin);
        }
    }

    private void checkWinBlockCollision() {
        Entity winEntity = world.tiledMap.getEntity((int) (playerTr.pos.x + playerBc.bounds.x / 2), (int) (playerTr.pos.y + playerBc.bounds.y / 2), TiledMap.WIN);
        if (winEntity != null) {
            screen.win();
        }
    }

    private void checkEnemyCollision() {
        for (Entity enemy : enemies) {
            final TransformComponent enemyTr = transformMapper.get(enemy);
            final BoundsComponent enemyBc = boundsMapper.get(enemy);

            if (playerOverlaps(enemyTr, enemyBc)) {
                screen.killed();
                break;
            }
        }
    }

    private void checkRectangleCollisionInsideCell(byte type) {
        checkPointAgainstRectangleInsideCellCollision((int)playerTr.pos.x, (int)playerTr.pos.y, type);
        checkPointAgainstRectangleInsideCellCollision((int)(playerTr.pos.x + playerBc.bounds.width), (int)playerTr.pos.y, type);
        checkPointAgainstRectangleInsideCellCollision((int)playerTr.pos.x, (int)(playerTr.pos.y + playerBc.bounds.height ), type);
        checkPointAgainstRectangleInsideCellCollision((int)(playerTr.pos.x + playerBc.bounds.width), (int)(playerTr.pos.y + playerBc.bounds.height), type);
    }

    private void checkPointAgainstRectangleInsideCellCollision(int x, int y, byte type) {
        Entity spike = world.tiledMap.getEntity(x, y, type);
        if (spike != null && playerOverlaps(spike.getComponent(TransformComponent.class), spike.getComponent(BoundsComponent.class))) {
            screen.killed();
        }
    }

    private boolean playerOverlaps(TransformComponent enemyTr, BoundsComponent enemyBc) {
        final float enemyX = enemyTr.pos.x + enemyBc.bounds.x;
        final float enemyY = enemyTr.pos.y + enemyBc.bounds.y;
        return     playerTr.pos.x < enemyX + enemyBc.bounds.width
                && playerTr.pos.x + playerBc.bounds.width > enemyX
                && playerTr.pos.y < enemyY + enemyBc.bounds.height
                && playerTr.pos.y + playerBc.bounds.height > enemyY;
    }

    private void checkWallCollisionAndRespond() {
        final TransformComponent playerTr = transformMapper.get(player);
        final BoundsComponent playerBc = boundsMapper.get(player);

        checkVertexAndRespond(playerTr, playerBc, 0, 0);
        checkVertexAndRespond(playerTr, playerBc, playerBc.bounds.width, 0);
        checkVertexAndRespond(playerTr, playerBc, 0, playerBc.bounds.height);
        checkVertexAndRespond(playerTr, playerBc, playerBc.bounds.width, playerBc.bounds.height);

    }

    private void checkVertexAndRespond(TransformComponent playerTr, BoundsComponent playerBc, float deltaX, float deltaY) {
        float changeX = checkCollisionAxisX(playerTr, playerBc, deltaX, deltaY);
        float changeY = checkCollisionAxisY(playerTr, playerBc, deltaX, deltaY);

        if (Math.abs(changeX - playerTr.lastPos.x) < Math.abs(changeY - playerTr.lastPos.y)) {
            playerTr.pos.x = changeX;
            playerTr.pos.y = checkCollisionAxisY(playerTr, playerBc, deltaX, deltaY);
        } else {
            playerTr.pos.y = changeY;
            playerTr.pos.x = checkCollisionAxisX(playerTr, playerBc, deltaX, deltaY);
        }
    }

    private float checkCollisionAxisX(TransformComponent playerTr, BoundsComponent playerBc, float deltaX, float deltaY) {
        final int cellX = (int) (playerTr.pos.x + deltaX);
        final int cellY = (int) (playerTr.pos.y + deltaY);

        if (world.tiledMap.getEntity(cellX, cellY, TiledMap.WALL) != null && (int) (playerTr.lastPos.x + deltaX) != cellX) {
            if (playerTr.lastPos.x < playerTr.pos.x) {
                return cellX - playerBc.bounds.width - PADDING;
            } else if (playerTr.lastPos.x > playerTr.pos.x) {
                return cellX + 1 + PADDING;
            }
        }
        return playerTr.pos.x;
    }

    private float checkCollisionAxisY(TransformComponent playerTr, BoundsComponent playerBc, float deltaX, float deltaY) {
        final int cellX = (int) (playerTr.pos.x + deltaX);
        final int cellY = (int) (playerTr.pos.y + deltaY);

        if (world.tiledMap.getEntity(cellX, cellY, TiledMap.WALL) != null && (int) (playerTr.lastPos.y + deltaY) != cellY) {
            if (playerTr.lastPos.y < playerTr.pos.y) {
                return cellY - playerBc.bounds.height - PADDING;
            } else if (playerTr.lastPos.y > playerTr.pos.y) {
                return cellY + 1 + PADDING;
            }
        }
        return playerTr.pos.y;
    }

    @Override
    public void addedToEngine(final Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(Families.BOUNDS, new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                final TransformComponent tc = transformMapper.get(entity);

                if (entity.getComponent(EnemyComponent.class) != null) {
                    enemies.add(entity);
                } else if (entity.getComponent(PlayerComponent.class) != null) {
                    player = entity;
                    playerTr = tc;
                    playerBc = boundsMapper.get(player);
                }
            }

            @Override
            public void entityRemoved(Entity entity) {
                final TransformComponent tc = transformMapper.get(entity);
                if (entity.getComponent(EnemyComponent.class) != null) {
                    enemies.remove(entity);
                } else if (entity.getComponent(PlayerComponent.class) != null) {
                    player = null;
                }
            }
        });
    }
}
