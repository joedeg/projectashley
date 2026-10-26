package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.jdegnan.projectashley.components.AreaTransitionComponent;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.level.LevelEntityFactory;
import com.jdegnan.projectashley.level.LevelObjectData;

public class AreaTransitionFactory implements LevelEntityFactory {
    @Override
    public Entity create(LevelObjectData object) {
        Entity entity = new Entity();

        Vector2 position = object.getPosition();

        PositionComponent positionComponent =
            new PositionComponent();
        positionComponent.x = position.x;
        positionComponent.y = position.y;
        entity.add(positionComponent);

        AreaTransitionComponent transition =
            new AreaTransitionComponent();
        transition.destinationLevelId =
            object.getStringProperty("destinationLevelId", null);

        transition.spawnX = object.getFloatProperty("spawnX", 0f);
        transition.spawnY = object.getFloatProperty("spawnY", 0f);
        entity.add(transition);

        ColliderComponent collider = new ColliderComponent();
        Vector2 size = object.getSize();

        collider.localBounds.set(0f, 0f, size.x, size.y);
        entity.add(collider);

        return entity;

    }
}
