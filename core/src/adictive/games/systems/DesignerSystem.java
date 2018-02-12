package adictive.games.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import adictive.games.SquareWorld;
import adictive.games.TiledMap;
import adictive.games.components.BlackHoleComponent;
import adictive.games.components.BoundsComponent;
import adictive.games.components.CoinComponent;
import adictive.games.components.EnemyComponent;
import adictive.games.components.IceFloorComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.SpikeComponent;
import adictive.games.components.TextureComponent;
import adictive.games.components.TransformComponent;
import adictive.games.components.WallComponent;
import adictive.games.components.WinComponent;
import adictive.games.level.LevelWriter;
import adictive.games.screens.PlayScreen;
import adictive.games.utils.GameData;

import static adictive.games.components.WallComponent.FLAG_CHAINBOX;

public class DesignerSystem extends EntitySystem implements Reseteable {
    private static final Vector3 UP    = new Vector3( 0f,  1f, 0f);
    private static final Vector3 DOWN  = new Vector3( 0f, -1f, 0f);
    private static final Vector3 LEFT  = new Vector3(-1f,  0f, 0f);
    private static final Vector3 RIGHT = new Vector3( 1f,  0f, 0f);

    private static final Color LIGHT_RED = new Color(0xffa3b7ff);

    private final SquareWorld world;
    private PlayScreen screen;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();

    private final Family transformable = Family.all(BoundsComponent.class, TransformComponent.class).get();
    private final Family enemiesFamily = Family.all(EnemyComponent.class).get();

    private final Vector3 lastTouch = new Vector3(0,0,0);
    private final Vector3 touch = new Vector3(0,0,0);
    private final Vector3 cursor = new Vector3(0,0,0);

    private Brush[] brushes;

    private LevelWriter levelWriter = new LevelWriter();

    private int state = 0;

    public DesignerSystem(SquareWorld world, PlayScreen screen) {
        super(11);
        this.world = world;
        this.screen = screen;

        this.brushes = new Brush[]{
                new WallBrush(0,0, 0),
                new WallBrush(0,0, FLAG_CHAINBOX),
                new WinBrush(0,0),
                new EnemyBrush(0,0),
                new CoinBrush(0,0),
                new IceFloorBrush(0, 0),
                new SpikeBrush(0,0, 0f),
                new SpikeBrush(0,0, 90f),
                new SpikeBrush(0,0, 180f),
                new SpikeBrush(0,0, 270f),
                new BlackHoleBrush(0,0, 4f),
                new PlayerBrush(world, 0,0)
        };
    }

    @Override
    public void update(float deltaTime) {
        updateCursor();
        updateCameraPosition();
        updateDesignState();
        drawHelpers();
    }

    private void updateCursor() {
        cursor.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        world.getCamera().unproject(cursor);

        if(Gdx.input.justTouched()) {
            lastTouch.set(cursor);
        }

        if(Gdx.input.isTouched()) {
            touch.set(cursor);
        }
    }

    private void updateDesignState() {
        final Brush brush = brushes[state];

        if (brush.shouldBeDelegatedTo()) {
            brush.process();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            state -= 1;
            if (state < 0) state = brushes.length -1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            state += 1;
            if (state >= brushes.length) state = 0;
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            drawBrushPreview(brush);
        }

        applyActions(brush);
    }

    private void drawHelpers() {
        final Brush brush = brushes[state];

        shapeRenderer.setProjectionMatrix(world.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.FIREBRICK);

        drawGrid();

        if (brush.shouldBeDelegatedTo()) {
            brush.drawHelpers();
        }

        drawEnemyTrayectories();

        shapeRenderer.end();
    }

    private void drawEnemyTrayectories() {
        ImmutableArray<Entity> enemies = getEngine().getEntitiesFor(enemiesFamily);
        shapeRenderer.setColor(LIGHT_RED);
        for (Entity e: enemies) {
            final EnemyComponent ec = e.getComponent(EnemyComponent.class);
            if (ec.end.x != 0 && ec.end.y != 0) {
                shapeRenderer.line(ec.start.x, ec.start.y, ec.end.x, ec.end.y);
            }
        }
    }

    private void applyActions(Brush brush) {

        if (Gdx.input.isTouched()) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                brush.erase();
            } else {
                brush.paint();
            }
        }

        // Paint a single time in order to avoid generating too many elements
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            brush.paint();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            saveLevel();
        }
    }

    private void drawBrushPreview(Brush brush) {
        batch.setProjectionMatrix(world.getCamera().combined);
        batch.begin();
        batch.setColor(1f,1f,1f, 0.5f);

        TransformComponent tc = brush.getSize();
        batch.draw(
                brush.getTextureRegion(),
                cursor.x - tc.size.x/2,
                cursor.y - tc.size.y/2,
                tc.size.x/2,
                tc.size.y/2,
                tc.size.x,
                tc.size.y,
                tc.scale.x,
                tc.scale.y,
                tc.rotation
        );

        batch.end();
    }

    private void saveLevel() {
        levelWriter.write(getEngine(), GameData.getCurrentLevel());
    }

    private void drawGrid() {
        shapeRenderer.setColor(Color.LIGHT_GRAY);

        for (int x = 0; x < world.getWidth(); x++) {
            shapeRenderer.line(x, 0, x, world.getHeight());
        }

        for (int y = 0; y < world.getHeight(); y++) {
            shapeRenderer.line(0, y, world.getWidth(), y);
        }
    }

    private void updateCameraPosition() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            world.getCamera().position.add(UP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            world.getCamera().position.add(DOWN);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            world.getCamera().position.add(LEFT);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            world.getCamera().position.add(RIGHT);
        }
    }

    @Override
    public void reset() {
        // DO nothing. Does not hold game state.
    }

    class Brush {
        protected Entity entity;

        Brush(Entity entity) {
            this.entity = entity;
        }

        TransformComponent getSize() {
            return entity.getComponent(TransformComponent.class);
        }

        TextureRegion getTextureRegion() {
            return entity.getComponent(TextureComponent.class).region;
        }

        public void paint() {

        }

        public boolean shouldBeDelegatedTo() {
            return false;
        }

        public void process() {

        }

        public void drawHelpers() {

        }

        void erase() {
            final ImmutableArray<Entity> entities = getEngine().getEntitiesFor(transformable);
            final Rectangle body = new Rectangle();
            for (Entity entity : entities) {
                final TransformComponent tc = entity.getComponent(TransformComponent.class);
                body.set(tc.pos.x, tc.pos.y, tc.size.x, tc.size.y);

                if (body.contains(touch.x, touch.y)) {
                    getEngine().removeEntity(entity);
                }
            }
        }
    }

    class CoinBrush extends Brush {

        CoinBrush(float x, float y) {
            super(CoinComponent.create(x,y));
        }

        @Override
        public void paint() {
            final float dstX = cursor.x - CoinComponent.WIDTH/2;
            final float dstY = cursor.y - CoinComponent.WIDTH/2;

            if (world.tiledMap.getEntity((int) dstX, (int) dstY, TiledMap.COIN) != null) return;

            CoinComponent.addNew(getEngine(), dstX, dstY);
        }
    }

    class EnemyBrush extends Brush {

        private static final int NAVIGATION_MODE = 0;
        private static final int ENEMY_END_POSITION_MODE = 2;
        private static final int ENEMY_INITIAL_POSITION_MODE = 3;

        private Entity enemy;
        private int editionMode = NAVIGATION_MODE;

        EnemyBrush(float x, float y) {
            super(EnemyComponent.create(x,y));
        }

        @Override
        public boolean shouldBeDelegatedTo() {
            return editionMode != NAVIGATION_MODE;
        }

        @Override
        public void drawHelpers() {
            if (editionMode == ENEMY_END_POSITION_MODE) {
                final EnemyComponent ec = enemy.getComponent(EnemyComponent.class);
                shapeRenderer.line(ec.start.x, ec.start.y, cursor.x, cursor.y);
            } else if (editionMode == ENEMY_INITIAL_POSITION_MODE) {
                final EnemyComponent ec = enemy.getComponent(EnemyComponent.class);
                shapeRenderer.line(ec.start.x, ec.start.y, ec.end.x, ec.end.y);
            }
        }

        @Override
        public void process() {
            if (editionMode == ENEMY_END_POSITION_MODE &&
                    (Gdx.input.isKeyJustPressed(Input.Keys.F) || Gdx.input.justTouched())) {
                EnemyComponent ec = enemy.getComponent(EnemyComponent.class);
                ec.end.set(cursor.x, cursor.y);
                editionMode = ENEMY_INITIAL_POSITION_MODE;
            } else if (editionMode == ENEMY_INITIAL_POSITION_MODE) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    EnemyComponent ec = enemy.getComponent(EnemyComponent.class);
                    ec.resetDirection();
                    EnemySystem.moveEnemyToPos(enemy, ec, ec.posInLine - 0.5f);
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                    EnemyComponent ec = enemy.getComponent(EnemyComponent.class);
                    ec.resetDirection();
                    EnemySystem.moveEnemyToPos(enemy, ec, ec.posInLine + 0.5f);
                    ec.initialPosInLine = ec.posInLine + 0.5f;
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                    enemy = null;
                    editionMode = NAVIGATION_MODE;
                }
            }
        }

        @Override
        public void paint() {
            this.enemy = EnemyComponent.addNew(getEngine(), cursor.x, cursor.y);
            enemy.getComponent(EnemyComponent.class).start.set(cursor.x, cursor.y);
            editionMode = ENEMY_END_POSITION_MODE;
        }
    }

    class WallBrush extends Brush {

        private int flag;

        WallBrush(int x, int y, int flag) {
            super(WallComponent.create(x,y, flag));
            this.flag = flag;
        }

        @Override
        public void paint() {
            final int x = (int)cursor.x;
            final int y = (int)cursor.y;

            if (world.tiledMap.getEntity(x, y, TiledMap.WALL) != null) return;

            WallComponent.addNewWall(getEngine(), x, y, flag);
        }
    }

    class WinBrush extends Brush {

        WinBrush(int x, int y) {
            super(WinComponent.create(x,y));
        }

        @Override
        public void paint() {
            final int x = (int)cursor.x;
            final int y = (int)cursor.y;

            if (world.tiledMap.getEntity(x, y, TiledMap.WIN) != null) return;

            WinComponent.addNew(getEngine(), x, y);
        }
    }

    class IceFloorBrush extends Brush {

        IceFloorBrush(int x, int y) {
            super(IceFloorComponent.create(x, y));
        }

        @Override
        public void paint() {
            final int x = (int) cursor.x;
            final int y = (int) cursor.y;

            if (world.tiledMap.getEntity(x, y, TiledMap.ICE) != null) return;

            IceFloorComponent.addNew(getEngine(), x, y);
        }
    }

    class PlayerBrush extends Brush {

        PlayerBrush(SquareWorld world, float x, float y) {
            super(PlayerComponent.create(world, x, y));
        }

        @Override
        public void paint() {
            PlayerComponent.addNew(world, getEngine(), cursor.x- PlayerComponent.WIDTH/2, cursor.y - PlayerComponent.WIDTH/2);
        }
    }

    class SpikeBrush extends Brush {
        private float rotation;

        SpikeBrush(int x, int y, float rotation) {
            super(SpikeComponent.create(x, y, rotation));
            this.rotation = rotation;
        }

        @Override
        public void paint() {
            final int x = (int)cursor.x;
            final int y = (int)cursor.y;

            if (world.tiledMap.getEntity(x, y, TiledMap.SPIKE) != null) return;

            SpikeComponent.addNew(getEngine(), x, y, rotation);
        }
    }

    class BlackHoleBrush extends Brush {
        private float attraction;

        BlackHoleBrush(int x, int y, float attraction) {
            super(BlackHoleComponent.create(x, y, attraction));
            this.attraction = attraction;
        }

        @Override
        public void paint() {
            final float x = ((int)cursor.x - BlackHoleComponent.SIDE/2) + 0.5f;
            final float y = ((int)cursor.y - BlackHoleComponent.SIDE/2) + 0.5f;

            if (world.tiledMap.getEntity((int) x, (int) y, TiledMap.HOLE) != null) return;

            BlackHoleComponent.addNew(getEngine(), x, y, attraction);
        }
    }

}
