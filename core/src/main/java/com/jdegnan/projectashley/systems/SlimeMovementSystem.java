package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.CollisionComponent;
import com.jdegnan.projectashley.components.EnemyDeathComponent;
import com.jdegnan.projectashley.components.EnemyMovementComponent;
import com.jdegnan.projectashley.components.KnockbackComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;
import com.jdegnan.projectashley.components.VelocityComponent;

public class SlimeMovementSystem extends IteratingSystem {

    private final ComponentMapper<EnemyMovementComponent> mm =
        ComponentMapper.getFor(EnemyMovementComponent.class);

    private final ComponentMapper<VelocityComponent> vm =
        ComponentMapper.getFor(VelocityComponent.class);

    private final ComponentMapper<CollisionComponent> cm =
        ComponentMapper.getFor(CollisionComponent.class);


    public SlimeMovementSystem() {
        super(Family.all(
            EnemyComponent.class,
            EnemyMovementComponent.class,
            VelocityComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        EnemyDeathComponent death =
            entity.getComponent(EnemyDeathComponent.class);

        if (death != null && death.dying) {
            VelocityComponent velocity =
                entity.getComponent(VelocityComponent.class);

            if (velocity != null) {
                velocity.vx = 0f;
                velocity.vy = 0f;
            }

            return;
        }


        KnockbackComponent knockback =
            entity.getComponent(KnockbackComponent.class);

        if (knockback != null && knockback.active) {
            return;
        }

        EnemyMovementComponent movement = mm.get(entity);

        VelocityComponent vel = vm.get(entity);

        CollisionComponent collision = cm.get(entity);

        if (collision.hitRight) {
            movement.directionX = -1f;
        } else if (collision.hitLeft) {
            movement.directionX = 1f;
        }

        if (collision.hitTop || collision.hitBottom) {
            movement.directionY *= -1f;
        }

        vel.vx = movement.directionX * movement.speed;
        vel.vy = movement.directionY * movement.speed;
    }
}
