package adictive.games.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import adictive.games.SquareWorld;
import adictive.games.components.BlackHoleComponent;
import adictive.games.components.CoinComponent;
import adictive.games.components.EnemyComponent;
import adictive.games.components.IceFloorComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.SpikeComponent;
import adictive.games.components.WallComponent;
import adictive.games.components.WinComponent;

public class LevelLoader {

    private SquareWorld world;
    private Engine engine;

    public LevelLoader(SquareWorld world, Engine engine) {
        this.world = world;
        this.engine = engine;
    }

    public void load(int levelNumber) {
        String fileName = "level" + levelNumber + ".txt";
        FileHandle fileHandle = Gdx.files.internal("levels/" + fileName);
        String s = fileHandle.readString();
        String[] lines = s.split("\n");
        for (String line : lines) {
            String[] entity = line.split(",");
            switch (entity[0]) {
                case "Block":
                    parseBlock(entity);
                    break;
                case "Enemy":
                    parseEnemy(entity);
                    break;
                case "Player":
                    parsePlayer(entity);
                    break;
                case "Win":
                    parseWin(entity);
                    break;
                case "Ice":
                    parseIce(entity);
                    break;
                case "Coin":
                    parseCoin(entity);
                    break;
                case "Spike":
                    parseSpike(entity);
                    break;
                case "Hole":
                    parseHole(entity);
                    break;
            }
        }
    }

    private void parseHole(String[] line) {
        BlackHoleComponent.addNew(
                engine,
                Float.parseFloat(line[1]), //x
                Float.parseFloat(line[2]), //y
                Float.parseFloat(line[3])       //attraction
        );
    }

    private void parseSpike(String[] line) {
        SpikeComponent.addNew(
                engine,
                (int)Float.parseFloat(line[1]), //x
                (int)Float.parseFloat(line[2]), //y
                Float.parseFloat(line[3])       //rotation
        );
    }

    private void parseCoin(String[] line) {
        CoinComponent.addNew(
                engine,
                Float.parseFloat(line[1]), Float.parseFloat(line[2])
        );
    }

    private void parseWin(String[] line) {
        WinComponent.addNew(
                engine,
                Float.parseFloat(line[1]), Float.parseFloat(line[2])
        );
    }

    private void parseIce(String[] line) {
        IceFloorComponent.addNew(
                engine,
                Float.parseFloat(line[1]), Float.parseFloat(line[2])
        );
    }

    private void parsePlayer(String[] line) {
        PlayerComponent.addNew(
                world, engine,
                Float.parseFloat(line[1]), Float.parseFloat(line[2])
        );
    }

    private void parseEnemy(String[] line) {
        EnemyComponent.addNew(
                engine, 0, 0,
                Float.parseFloat(line[1]),
                Float.parseFloat(line[2]),
                Float.parseFloat(line[3]),
                Float.parseFloat(line[4]),
                Float.parseFloat(line[5])
        );
    }

    private void parseBlock(String[] line) {
        loadBlock(world, engine, Integer.parseInt(line[1]), Integer.parseInt(line[2]), line.length >=4 ? Integer.parseInt(line[3]) : 0);
    }

    private static void loadBlock(SquareWorld world, Engine engine, int x, int y, int flags) {
        WallComponent.addNewWall(engine, x, y, flags);
    }
}
