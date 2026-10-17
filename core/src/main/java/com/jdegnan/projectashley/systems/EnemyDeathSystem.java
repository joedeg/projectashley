package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.jdegnan.projectashley.assets.animations.AnimationState;
import com.jdegnan.projectashley.components.AnimationSetComponent;
import com.jdegnan.projectashley.components.AnimationStateComponent;
import com.jdegnan.projectashley.components.EnemyDeathComponent;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;
import com.jdegnan.projectashley.factories.ParticleFactory;

public class EnemyDeathSystem extends EntitySystem {

    private static final float DEATH_DURATION = 1f;
    private final ComponentMapper<HealthComponent> hm =
        ComponentMapper.getFor(HealthComponent.class);

    private final ComponentMapper<EnemyDeathComponent> edm =
        ComponentMapper.getFor(EnemyDeathComponent.class);


    private ImmutableArray<Entity> enemies;

    private final ParticleFactory particleFactory;

    private final Array<Entity> deadEnemies = new Array<>();

    private Engine engine;

    public EnemyDeathSystem(ParticleFactory particleFactory) {
        this.particleFactory = particleFactory;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
        enemies =
            engine.getEntitiesFor(
                Family.all(
                    EnemyComponent.class,
                    HealthComponent.class,
                    EnemyDeathComponent.class
                ).get());
    }

    @Override
    public void update(float deltaTime) {
        deadEnemies.clear();


        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);

            HealthComponent health = hm.get(enemy);
            EnemyDeathComponent death = edm.get(enemy);

            /*
            Mark the enemy as dying.
             */
            if (!death.dying && health.hp <= 0) {
                death.dying = true;
                death.remainingTime = DEATH_DURATION;

                AnimationStateComponent anim =
                    enemy.getComponent(AnimationStateComponent.class);

                AnimationSetComponent animationSet =
                    enemy.getComponent(AnimationSetComponent.class);

                if (anim != null
                    && animationSet != null
                    && animationSet.set.get(AnimationState.DIE) != null) {

                    anim.state = AnimationState.DIE;
                    anim.stateTime = 0f;
                    anim.looping = false;
                }


            }

            if (death.dying) {
                death.remainingTime -= deltaTime;


                AnimationStateComponent anim =
                    enemy.getComponent(AnimationStateComponent.class);

                AnimationSetComponent animationSet =
                    enemy.getComponent(AnimationSetComponent.class);

                if (!death.burstCreated
                    && anim != null
                    && animationSet != null
                    && animationSet.set != null) {

                    Animation<TextureRegion> dyingAnimation =
                        animationSet.set.get(AnimationState.DIE);

                    if (dyingAnimation != null
                        && anim.state == AnimationState.DIE
                        && anim.stateTime >= dyingAnimation.getAnimationDuration()) {

                        PositionComponent pos =
                            enemy.getComponent(PositionComponent.class);

                        if (pos != null) {
                            float centerX = pos.x + 13.5f;
                            float centerY = pos.y + 8f;
                            particleFactory.createBurst(centerX, centerY, 12);
                        }

                        death.burstCreated = true;
                    }
                }


                if (death.remainingTime <= 0) {
                    deadEnemies.add(enemy);
                }
            }
        }

        for (int i = 0; i < deadEnemies.size; i++) {
            engine.removeEntity(deadEnemies.get(i));
        }
    }
}
