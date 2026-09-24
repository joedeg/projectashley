package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.TagComponents.DestroyComponent;

public class HealthSystem extends IteratingSystem {

    ComponentMapper<HealthComponent> hm =
        ComponentMapper.getFor(HealthComponent.class);
    public HealthSystem() {
        super(Family.all(
            HealthComponent.class
        ).get());


    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent health = hm.get(entity);

        if(health.hp <= 0){
            entity.add(new DestroyComponent());
        }
    }
}
