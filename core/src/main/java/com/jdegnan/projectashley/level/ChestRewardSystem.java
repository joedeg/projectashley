package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.ChestComponent;

public class ChestRewardSystem extends IteratingSystem {

    private final ComponentMapper<ChestComponent> cm =
        ComponentMapper.getFor(ChestComponent.class);

    public ChestRewardSystem() {
        super(Family.all(ChestComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ChestComponent chest = cm.get(entity);

        if(!chest.opened){
            return;
        }

        if(chest.item == null){
            return;
        }

        System.out.println("Rewarding " + chest.item);

        chest.item = null;

    }
}
