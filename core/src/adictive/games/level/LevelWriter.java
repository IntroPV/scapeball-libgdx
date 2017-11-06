package adictive.games.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import adictive.games.components.CoinComponent;
import adictive.games.components.EnemyComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TransformComponent;
import adictive.games.components.WallComponent;
import adictive.games.components.WinComponent;

public class LevelWriter {
    private final String fileName;

    public LevelWriter(int level) {
        this.fileName = "level" + level + ".txt";
    }

    public void write(final Engine engine) {
        PrintWriter writer;
        try {
            writer = new PrintWriter("levels/" + fileName, "UTF-8");
            ImmutableArray<Entity> entities = engine.getEntities();
            for (Entity e : entities) {
                entityToCSV(writer, e);
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void entityToCSV(PrintWriter writer, Entity e) {
        if (e.getComponent(WallComponent.class) != null) {
            writer.println(blockToCSV(e));
        } else if (e.getComponent(EnemyComponent.class) != null) {
            writer.println(enemyToCSV(e));
        } else if (e.getComponent(PlayerComponent.class) != null) {
            writer.println(playerToCSV(e));
        } else if (e.getComponent(WinComponent.class) != null) {
            writer.println(winBlockToCSV(e));
        } else if (e.getComponent(CoinComponent.class) != null) {
            writer.println(coinToCSV(e));
        }
    }

    private String coinToCSV(Entity e) {
        TransformComponent tc = e.getComponent(TransformComponent.class);
        return csv("Coin",tc.pos.x, tc.pos.y) ;
    }

    private String winBlockToCSV(Entity e) {
        TransformComponent tc = e.getComponent(TransformComponent.class);
        return csv("Win",tc.pos.x, tc.pos.y) ;
    }

    public String playerToCSV(Entity e) {
        TransformComponent tc = e.getComponent(TransformComponent.class);
        return csv("Player",tc.pos.x, tc.pos.y) ;
    }

    public String enemyToCSV(Entity e) {
        EnemyComponent ec = e.getComponent(EnemyComponent.class);
        return csv("Enemy",ec.initialPosInLine,ec.start.x,ec.start.y,ec.end.x,ec.end.y) ;
    }

    public String blockToCSV(Entity e) {
        TransformComponent tc = e.getComponent(TransformComponent.class);
        return csv("Block", (int)tc.pos.x, (int)tc.pos.y);
    }

    private static String csv(Object... e) {
        final StringBuilder sb = new StringBuilder();
        for (Object s : e) {
            sb.append(s.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
