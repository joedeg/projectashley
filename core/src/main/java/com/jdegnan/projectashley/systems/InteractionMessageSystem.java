package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.InteractionMessageComponent;

public class InteractionMessageSystem extends IteratingSystem {

    private final ComponentMapper<InteractionMessageComponent> mm =
        ComponentMapper.getFor(InteractionMessageComponent.class);
    public InteractionMessageSystem() {
        super(Family.all(
            InteractionMessageComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InteractionMessageComponent message = mm.get(entity);

        if(message.remainingTime <= 0f){
            return;
        }

        message.remainingTime -= deltaTime;

        if(message.remainingTime <= 0f){
            message.clear();
        }
    }
}
