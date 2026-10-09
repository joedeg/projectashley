package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.WallComponent;

public class DebugCollisionRenderSystem
    extends IteratingSystem {

    private final ShapeRenderer renderer;
    private final OrthographicCamera camera;

    private final ComponentMapper<ColliderComponent> cm =
        ComponentMapper.getFor(
            ColliderComponent.class);

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    public DebugCollisionRenderSystem(OrthographicCamera camera) {

        super(Family.all(
            ColliderComponent.class,
            PositionComponent.class
        ).get());

        this.camera = camera;
        renderer = new ShapeRenderer();
    }

    @Override
    public void update(float deltaTime) {
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(
            ShapeRenderer.ShapeType.Line);

        super.update(deltaTime);

        renderer.end();
    }

    @Override
    protected void processEntity(
        Entity entity,
        float deltaTime) {

        ColliderComponent col =
            cm.get(entity);

        float x;
        float y;

        if(entity.getComponent(WallComponent.class) != null){
            x = col.localBounds.x;
            y = col.localBounds.y;
        } else {
            PositionComponent pos = pm.get(entity);
            x = pos.x + col.localBounds.x;
            y = pos.y + col.localBounds.y;
        }

        renderer.rect(
            x,
            y,
            col.localBounds.width,
            col.localBounds.height
        );
    }

    @Override
    public void removedFromEngine(Engine engine) {
        renderer.dispose();
        super.removedFromEngine(engine);
    }
}
