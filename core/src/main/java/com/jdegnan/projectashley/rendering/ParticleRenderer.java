package com.jdegnan.projectashley.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jdegnan.projectashley.components.ParticleComponent;
import com.jdegnan.projectashley.components.PositionComponent;

public class ParticleRenderer {

    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private final ComponentMapper<ParticleComponent> pam =
        ComponentMapper.getFor(ParticleComponent.class);

    private ImmutableArray<Entity> particles;

    public ParticleRenderer(OrthographicCamera camera) {
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void addedToEngine(Engine engine) {
        particles = engine.getEntitiesFor(
            Family.all(
                PositionComponent.class,
                ParticleComponent.class
            ).get()
        );
    }

    public void render() {
        if (particles == null || particles.size() == 0) {
            return;
        }

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Entity entity : particles) {
            PositionComponent pos = pm.get(entity);
            ParticleComponent particle = pam.get(entity);

            float life = particle.remainingTime / particle.lifetime;

            shapeRenderer.setColor(
                1f,
                0.85f,
                0.25f,
                life
            );

            shapeRenderer.circle(
                pos.x,
                pos.y,
                particle.size
            );
        }

        shapeRenderer.end();

    }

}
