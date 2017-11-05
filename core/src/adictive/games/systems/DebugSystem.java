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

public class DebugSystem extends EntitySystem {

    private SquareWorld world;
    private ShapeRenderer shapeRenderer;

    private final Family boundsFamily = Family.all(TransformComponent.class, BoundsComponent.class).get();

    public DebugSystem(SquareWorld world) {
        this.world = world;
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
        shapeRenderer.rect(tr.pos.x, tr.pos.y, bc.bounds.width, bc.bounds.height);
    }
}