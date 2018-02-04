package adictive.games.systems;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import adictive.games.SquareWorld;
import adictive.games.components.BoundsComponent;
import adictive.games.components.TransformComponent;
import adictive.games.screens.PlayScreen;
import adictive.games.utils.GameData;

public class DebugSystem extends EntitySystem implements Reseteable {

    private SquareWorld world;
    private PlayScreen screen;
    private ShapeRenderer shapeRenderer;

    private final Family boundsFamily = Family.all(TransformComponent.class, BoundsComponent.class).get();

    public DebugSystem(SquareWorld world, PlayScreen screen) {
        this.world = world;
        this.screen = screen;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float deltaTime) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            world.getCamera().zoom += 0.1f;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            world.getCamera().zoom -= 0.1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            GameData.decrementCurrentLevel();
            screen.state = PlayScreen.DEBUG_CHANGE_LEVEL;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            GameData.incrementCurrentLevel();
            screen.state = PlayScreen.DEBUG_CHANGE_LEVEL;
        }

        shapeRenderer.setProjectionMatrix(world.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        ImmutableArray<Entity> walls = getEngine().getEntitiesFor(boundsFamily);

        shapeRenderer.setColor(Color.GREEN);

        for (Entity wall : walls) {
            renderBounds(wall);
        }

        shapeRenderer.end();

    }

    private void renderBounds(Entity entity) {
        TransformComponent tr = entity.getComponent(TransformComponent.class);
        BoundsComponent bc = entity.getComponent(BoundsComponent.class);
        shapeRenderer.rect(tr.pos.x + bc.bounds.x, tr.pos.y+ bc.bounds.y, bc.bounds.width, bc.bounds.height);
    }

    @Override
    public void reset() {
        // do nothing. does not hold game state
    }
}
