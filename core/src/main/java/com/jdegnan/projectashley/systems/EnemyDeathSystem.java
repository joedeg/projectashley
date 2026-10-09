package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;

public class EnemyDeathSystem extends EntitySystem {
    private final ComponentMapper<HealthComponent> healthMapper =
        ComponentMapper.getFor(HealthComponent.class);

    private ImmutableArray<Entity> enemies;

    private final Array<Entity> deadEnemies = new Array<>();

    private Engine engine;

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
        enemies =
            engine.getEntitiesFor(
                Family.all(
                    EnemyComponent.class,
                    HealthComponent.class
                ).get());
    }

    @Override
    public void update(float deltaTime) {
        deadEnemies.clear();


        for(int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);

            HealthComponent health = healthMapper.get(enemy);

            if(health.hp <=0){
                deadEnemies.add(enemy);
            }
        }

        for(int i = 0; i < deadEnemies.size; i++){
            engine.removeEntity(deadEnemies.get(i));
        }
    }
}
