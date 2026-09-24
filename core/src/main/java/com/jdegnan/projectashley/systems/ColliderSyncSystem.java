package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;

public class ColliderSyncSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private ComponentMapper<ColliderComponent> cm =
        ComponentMapper.getFor(ColliderComponent.class);


    public ColliderSyncSystem( ) {
        super(Family.all(
            PositionComponent.class,
            ColliderComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PositionComponent pos = pm.get(entity);
        ColliderComponent col = cm.get(entity);

        col.localBounds.setPosition(pos.x, pos.y);
    }
}
