package adictive.games;

import com.badlogic.ashley.core.Entity;

import adictive.games.components.BlackHoleComponent;
import adictive.games.components.CoinComponent;
import adictive.games.components.SpikeComponent;
import adictive.games.components.WallComponent;
import adictive.games.components.WinComponent;
import adictive.games.systems.Reseteable;

public class TiledMap implements Reseteable {
    public Entity[][][] tiles;
    private static final int MAX_ENTITIES_ON_TILE = 4;

    public static final Class[] REGISTRY = new Class[6];

    public static final byte WALL = 1;
    public static final byte WIN = 2;
    public static final byte COIN = 3;
    public static final byte SPIKE = 4;
    public static final byte HOLE = 5;

    static {
        REGISTRY[WALL] = WallComponent.class;
        REGISTRY[WIN] = WinComponent.class;
        REGISTRY[COIN] = CoinComponent.class;
        REGISTRY[SPIKE] = SpikeComponent.class;
        REGISTRY[HOLE] = BlackHoleComponent.class;
    }

    public void init(int width, int height) {
        this.tiles = new Entity[width][height][REGISTRY.length];
    }

    /*
     * O(len(REGISTRY))
     */
    public void setEntity(int x, int y, Entity entity) {
        int entityConstant = getEntityConstant(entity);
        if (entityConstant >= 0) {
            tiles[x][y][entityConstant] = entity;
        }
    }

    public void removeEntity(int x, int y, Entity entity) {
        int entityConstant = getEntityConstant(entity);
        if (entityConstant >= 0) {
            tiles[x][y][entityConstant] = null;
        }
    }

    public Entity getEntity(int x, int y, byte entityType) {
        return this.tiles[x][y][entityType];
    }

    @SuppressWarnings("unchecked")
    private int getEntityConstant(Entity entity) {
        final int size = REGISTRY.length;
        for (int i = 0; i < size; i++) {
            if (REGISTRY[i] != null && entity.getComponent(REGISTRY[i]) != null) {
                return i;
            }
        }
        return -1;
    }

    public void reset() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                for (int k = 0; k < MAX_ENTITIES_ON_TILE; k++) {
                    tiles[i][j][k] = null;
                }
            }
        }
    }
}
