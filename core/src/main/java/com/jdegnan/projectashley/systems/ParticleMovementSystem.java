package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.ParticleComponent;
import com.jdegnan.projectashley.components.PositionComponent;

public class ParticleMovementSystem extends IteratingSystem {

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    public ParticleMovementSystem() {
        super(Family.all(
            PositionComponent.class,
            ParticleComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        ParticleComponent particle =
            entity.getComponent(ParticleComponent.class);

        // Update particle position based on velocity
        pos.x += particle.velocityX * deltaTime;
        pos.y += particle.velocityY * deltaTime;


        float speed = (float) Math.sqrt(
            particle.velocityX * particle.velocityX +
                particle.velocityY * particle.velocityY
        );


        if (speed > 0) {
            float newSpeed = Math.max(
                0f,
                speed - particle.drag * deltaTime
            );

            float scale = newSpeed / speed;

            particle.velocityX *= scale;
            particle.velocityY *= scale;
        }

        particle.remainingTime -= deltaTime;

        if (particle.remainingTime <= 0f) {
            getEngine().removeEntity(entity);
        }


    }
}
