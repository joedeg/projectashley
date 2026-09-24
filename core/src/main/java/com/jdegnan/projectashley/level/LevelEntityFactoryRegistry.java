package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.ObjectMap;

public class LevelEntityFactoryRegistry {

    private final ObjectMap<String, LevelEntityFactory> factories = new ObjectMap<>();

    public void register(
        String type,
        LevelEntityFactory factory){

        if(type == null){
            throw new IllegalArgumentException(
                "type cannot be null."
            );
        }

        if(factory == null){
            throw new IllegalArgumentException(
                "factory cannot be null."
            );
        }

        factories.put(type, factory);

    }

    public Entity create(MapObject object) {
        String type =
            object.getProperties()
                .get("type", String.class);

        if(type == null){
            throw new IllegalStateException(
                "Map object '" +
                    object.getName() +
                    "' has no 'type' property."

            );
        }

        LevelEntityFactory factory = factories.get(type);

        if(factory == null){
            throw new IllegalStateException(
                "No factory registered for type:" + type
            );
        }

        return factory.create(object);
    }
}
