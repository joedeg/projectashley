package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.jdegnan.projectashley.components.PositionComponent;

public class TestEntityFactory implements LevelEntityFactory {

    private final PooledEngine engine;

    public TestEntityFactory(PooledEngine engine){
        this.engine = engine;
    }
    @Override
    public Entity create(LevelObjectData object) {

        Entity entity = engine.createEntity();

        PositionComponent pos = engine.createComponent(PositionComponent.class);

        pos.x = object.getPosition().x;
        pos.y = object.getPosition().y;
        entity.add(pos);

        System.out.println(
            "Created test entity at "
                + pos.x
                + ", "
                + pos.y
        );

        return entity;
    }
}
