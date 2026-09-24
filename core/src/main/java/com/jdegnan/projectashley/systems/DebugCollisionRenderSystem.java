package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jdegnan.projectashley.components.ColliderComponent;

public class DebugCollisionRenderSystem
    extends IteratingSystem {

    private ShapeRenderer renderer;
    private OrthographicCamera camera;

    private ComponentMapper<ColliderComponent> cm =
        ComponentMapper.getFor(
            ColliderComponent.class);

    public DebugCollisionRenderSystem(OrthographicCamera camera) {

        super(Family.all(
            ColliderComponent.class
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

        renderer.rect(
            col.localBounds.x,
            col.localBounds.y,
            col.localBounds.width,
            col.localBounds.height
        );
    }
}
