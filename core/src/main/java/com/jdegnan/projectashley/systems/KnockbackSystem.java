package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.KnockbackComponent;
import com.jdegnan.projectashley.components.VelocityComponent;

public class KnockbackSystem extends IteratingSystem {

    private static final float KNOCKBACK_DURATION = 0.15f;

    private final ComponentMapper<KnockbackComponent> km =
        ComponentMapper.getFor(KnockbackComponent.class);

    private final ComponentMapper<VelocityComponent> vm =
        ComponentMapper.getFor(VelocityComponent.class);


    public KnockbackSystem() {
        super(Family.all(
            KnockbackComponent.class,
            VelocityComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        KnockbackComponent knockback = km.get(entity);
        VelocityComponent velocity = vm.get(entity);

        if(!knockback.active){
            return;
        }

        knockback.remainingTime -= deltaTime;

        if(knockback.remainingTime <= 0f){
            knockback.remainingTime = 0f;
            knockback.active = false;
            knockback.velocityX = 0f;
            knockback.velocityY = 0f;
            return;
        }

        velocity.vx = knockback.velocityX;
        velocity.vy = knockback.velocityY;
    }
}
