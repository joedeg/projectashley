package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
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


        for(int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);

            HealthComponent health = hm.get(enemy);
            EnemyDeathComponent death = edm.get(enemy);

            /*
            Mark the enemy as dying.
             */
            if (!death.dying && health.hp <= 0) {
                death.dying = true;
                death.remainingTime = DEATH_DURATION;

                PositionComponent pos =
                    enemy.getComponent(PositionComponent.class);
                if(pos != null){
                    float centerX = pos.x + 13.5f;
                    float centerY = pos.y + 8f;

                    particleFactory.createBurst(centerX, centerY, 12);
                }
            }

            if (death.dying) {
                death.remainingTime -= deltaTime;

                if (death.remainingTime <= 0f) {
                    deadEnemies.add(enemy);
                }
            }
        }

        for(int i = 0; i < deadEnemies.size; i++){
            engine.removeEntity(deadEnemies.get(i));
        }
    }
}
