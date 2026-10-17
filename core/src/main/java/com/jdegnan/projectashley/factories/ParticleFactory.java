package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.MathUtils;
import com.jdegnan.projectashley.components.ParticleComponent;
import com.jdegnan.projectashley.components.PositionComponent;

public class ParticleFactory {

    private final PooledEngine engine;

    public ParticleFactory(PooledEngine engine){
        this.engine = engine;
    }

    public Entity create(
        float x,
        float y,
        float velocityX,
        float velocityY,
        float lifetime,
        float size,
        float drag){

        Entity entity = engine.createEntity();

        PositionComponent pos =
            engine.createComponent(PositionComponent.class);

        pos.x = x;
        pos.y = y;

        ParticleComponent particle =
            engine.createComponent(ParticleComponent.class);

        particle.velocityX = velocityX;
        particle.velocityY = velocityY;
        particle.lifetime = lifetime;
        particle.size = size;
        particle.remainingTime = lifetime;
        particle.drag = drag;

        entity.add(pos);
        entity.add(particle);

        engine.addEntity(entity);

        return entity;
    }

    public void createBurst(
        float x,
        float y,
        int count
    ){

        for(int i = 0; i < count; i++){

            float angle = MathUtils.random(0f, MathUtils.PI2);

            float speed = MathUtils.random(40f, 75f);

            float velocityX = MathUtils.cos(angle) * speed;
            float velocityY = MathUtils.sin(angle) * speed;

            float size = MathUtils.random(3f, 5f);

            float lifetime = MathUtils.random(0.4f, 0.7f);

            float drag = 20f;

            create(
                x,
                y,
                velocityX,
                velocityY,
                lifetime,
                size,
                drag
            );
        }
    }
}
