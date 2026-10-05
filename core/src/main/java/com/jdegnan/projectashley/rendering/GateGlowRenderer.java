package com.jdegnan.projectashley.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.jdegnan.projectashley.components.BrazierGateComponent;
import com.jdegnan.projectashley.components.GateGlowComponent;
import com.jdegnan.projectashley.components.PositionComponent;

public class GateGlowRenderer {

    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<GateGlowComponent> gm =
        ComponentMapper.getFor(GateGlowComponent.class);

    private ImmutableArray<Entity> gates;

    public GateGlowRenderer(OrthographicCamera camera) {
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void addedToEngine(Engine engine) {
        gates = engine.getEntitiesFor(
            Family.all(
                BrazierGateComponent.class,
                GateGlowComponent.class,
                PositionComponent.class
            ).get()
        );
    }

    public void render() {
        if (gates == null || gates.size() == 0) {
            return;
        }

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Entity gateEntity : gates) {
            GateGlowComponent glow = gm.get(gateEntity);

            if (!glow.active) {
                continue;
            }

            PositionComponent pos = pm.get(gateEntity);

            float progress = glow.timer / glow.duration;

            float radius = glow.maxRadius * progress;

            float alpha = 1.0f - progress;

            Gdx.gl.glEnable(
                GL20.GL_BLEND
            );

            Gdx.gl.glBlendFunc(
                GL20.GL_SRC_ALPHA,
                GL20.GL_ONE_MINUS_SRC_ALPHA
            );

            shapeRenderer.setColor(
                1.0f,
                0.85f,
                0.25f,
                alpha * 0.35f
            );

            shapeRenderer.circle(
                pos.x + 16f,
                pos.y + 48f,
                radius
            );
        }

        shapeRenderer.end();

        Gdx.gl.glDisable(
            GL20.GL_BLEND
        );
    }

}


