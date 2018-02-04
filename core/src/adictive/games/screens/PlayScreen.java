package adictive.games.screens;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;

import adictive.games.MainGame;
import adictive.games.SquareWorld;
import adictive.games.components.CoinComponent;
import adictive.games.level.LevelLoader;
import adictive.games.systems.BlackHoleSystem;
import adictive.games.systems.CollisionSystem;
import adictive.games.systems.DebugSystem;
import adictive.games.systems.DesignerSystem;
import adictive.games.systems.EnemySystem;
import adictive.games.systems.FollowCameraSystem;
import adictive.games.systems.LightSystem;
import adictive.games.systems.MovementSystem;
import adictive.games.systems.PlayerInputSystem;
import adictive.games.systems.RenderingSystem;
import adictive.games.systems.Reseteable;
import adictive.games.utils.GameData;

public class PlayScreen extends ScreenAdapter implements Reseteable {
    private static final Family COINS_FAMILY = Family.all(CoinComponent.class).get();
    private final PooledEngine engine = new PooledEngine();
    private final SquareWorld world = new SquareWorld();
    public final MainGame superCubito;
    private LevelLoader levelLoader;
    public int state = RUNNING;

    public static final int RUNNING = 1;
    public static final int KILLED = 2;
    public static final int WIN = 3;
    public static final int DEBUG_CHANGE_LEVEL = 4;

    public PlayScreen(MainGame superCubito) {
        this.superCubito = superCubito;
        this.levelLoader = new LevelLoader(world, engine);
        generateSystems();
        reset();
    }

    public void reset() {
        pause(true);
        ImmutableArray<EntitySystem> systems = engine.getSystems();
        engine.removeAllEntities();
        for (EntitySystem system: systems ) {
            ((Reseteable) system).reset();
        }
        this.levelLoader.load(GameData.getCurrentLevel());
        pause(false);
        this.state = RUNNING;
    }

    public void generateSystems() {
        engine.addSystem(new FollowCameraSystem());

        engine.addSystem(new PlayerInputSystem());
        engine.addSystem(new EnemySystem());
        engine.addSystem(new BlackHoleSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new CollisionSystem(world, this));
        engine.addSystem(new RenderingSystem(world));
        engine.addSystem(new LightSystem(world));
        engine.addSystem(new DebugSystem(world, this));
        engine.addSystem(new DesignerSystem(world, this));

        getDesignerSystem().setProcessing(false);
        getDebugSystem().setProcessing(false);

    }

    public void killed() {
        this.state = KILLED;
    }

    public void win() {
        if (engine.getEntitiesFor(COINS_FAMILY).size() == 0 ) {
            this.state = WIN;
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        engine.getSystem(RenderingSystem.class).resize(width,height);
        engine.getSystem(LightSystem.class).resize(width,height);
    }

    public void updateMode() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            getDebugSystem().setProcessing(!getDebugSystem().checkProcessing());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            DesignerSystem designerSystem = getDesignerSystem();
            boolean isDesignerActive = !designerSystem.checkProcessing();
            designerSystem.setProcessing(isDesignerActive);
            pause(isDesignerActive);
        }
    }

    private void pause(boolean pause) {
        engine.getSystem(MovementSystem.class).setProcessing(!pause);
        engine.getSystem(CollisionSystem.class).setProcessing(!pause);
        engine.getSystem(FollowCameraSystem.class).setProcessing(!pause);
        engine.getSystem(PlayerInputSystem.class).setProcessing(!pause);
        engine.getSystem(EnemySystem.class).setProcessing(!pause);
        engine.getSystem(LightSystem.class).setProcessing(!pause);
        engine.getSystem(BlackHoleSystem.class).setProcessing(!pause);
    }

    private DebugSystem getDebugSystem() {
        return engine.getSystem(DebugSystem.class);
    }

    private DesignerSystem getDesignerSystem() {
        return engine.getSystem(DesignerSystem.class);
    }

    @Override
    public void render (float delta) {
        updateMode();
        switch (state) {
            case RUNNING:
                engine.update(delta);
                break;
            case KILLED:
                reset();
                superCubito.analyticsManager.loseLevel(GameData.getCurrentLevel());
                break;
            case WIN:
                int level = GameData.incrementCurrentLevel();
                superCubito.analyticsManager.winLevel(level);
                reset();
                break;
            case DEBUG_CHANGE_LEVEL:
                reset();
                break;
                
        }
    }

}
