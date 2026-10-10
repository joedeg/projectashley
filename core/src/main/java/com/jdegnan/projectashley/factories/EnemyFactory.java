package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import com.jdegnan.projectashley.assets.Assets;

import com.jdegnan.projectashley.assets.animations.AnimationState;
import com.jdegnan.projectashley.assets.animations.SlimeAnimationRegistry;
import com.jdegnan.projectashley.components.AnimationSetComponent;
import com.jdegnan.projectashley.components.AnimationStateComponent;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.CollisionComponent;
import com.jdegnan.projectashley.components.DamageFlashComponent;
import com.jdegnan.projectashley.components.EnemyDeathComponent;
import com.jdegnan.projectashley.components.EnemyMovementComponent;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.KnockbackComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.SpriteComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;
import com.jdegnan.projectashley.components.VelocityComponent;
import com.jdegnan.projectashley.level.LevelEntityFactory;
import com.jdegnan.projectashley.level.LevelObjectData;

public class EnemyFactory implements LevelEntityFactory {
    private final PooledEngine engine;
    private final Assets assets;

    public EnemyFactory(
        PooledEngine engine,
        Assets assets) {

        this.engine = engine;
        this.assets = assets;
    }

    @Override
    public Entity create(LevelObjectData object) {

        Entity enemy = engine.createEntity();

        PositionComponent pos =
            engine.createComponent(PositionComponent.class);

        pos.x = object.getPosition().x;
        pos.y = object.getPosition().y;

        EnemyComponent enemyComponent =
            engine.createComponent(EnemyComponent.class);

        enemyComponent.enemyType =
            object.getStringProperty(
                "enemyType",
                "unknown"
            );

        AnimationSetComponent animSet =
            engine.createComponent(AnimationSetComponent.class);

        animSet.set = new SlimeAnimationRegistry().create(assets);

        AnimationStateComponent animState =
            engine.createComponent(AnimationStateComponent.class);

        animState.state = AnimationState.IDLE;
        animState.stateTime = 0f;
        animState.looping = true;

        SpriteComponent sprite =
            engine.createComponent(SpriteComponent.class);

        sprite.textureRegion =
            animSet.set.get(
                AnimationState.IDLE
            ).getKeyFrame(0f, true);


        RenderComponent renderComponent =
            engine.createComponent(RenderComponent.class);

        renderComponent.width = 27;
        renderComponent.height = 16;
        renderComponent.layer = 0;

        ColliderComponent collider =
            engine.createComponent(ColliderComponent.class);


        collider.localBounds.set(
            0,
            0,
            27,
            16);

        VelocityComponent vel =
            engine.createComponent(VelocityComponent.class);

        EnemyMovementComponent movement =
            engine.createComponent(EnemyMovementComponent.class);

        movement.speed = 20f;
        movement.directionX = 1f;
        movement.directionY = 0f;

        CollisionComponent collision =
            engine.createComponent(CollisionComponent.class);

        HealthComponent health =
            engine.createComponent(HealthComponent.class);

        DamageFlashComponent damageFlash =
            engine.createComponent(DamageFlashComponent.class);

        KnockbackComponent knockback =
            engine.createComponent(KnockbackComponent.class);

        EnemyDeathComponent death =
            engine.createComponent(EnemyDeathComponent.class);

        enemy.add(death);
        enemy.add(knockback);
        enemy.add(damageFlash);
        enemy.add(health);
        enemy.add(collision);
        enemy.add(movement);
        enemy.add(vel);
        enemy.add(collider);
        enemy.add(renderComponent);
        enemy.add(sprite);
        enemy.add(animSet);
        enemy.add(animState);
        enemy.add(pos);
        enemy.add(enemyComponent);

        return enemy;
    }
}
