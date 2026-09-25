package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;


public class LevelEntityLoader {
    private final LevelEntityFactoryRegistry factoryRegistry;

    public LevelEntityLoader(
        LevelEntityFactoryRegistry factoryRegistry) {
        this.factoryRegistry = factoryRegistry;
    }

    public void load(
        TiledMap map,
        PooledEngine engine,
        LevelRuntime runtime
    ) {
        MapLayer layer = map.getLayers().get("Entities");

        if (layer == null) {
            return;
        }

        for (MapObject mapObject : layer.getObjects()) {

            LevelObjectData object = createObjectData(mapObject);
            Entity entity = factoryRegistry.create(object);


            if (entity == null) {
                continue;
            }

            runtime.addEntity(
                engine,
                entity
            );
        }
    }

    private LevelObjectData createObjectData(MapObject mapObject) {

        if (!(mapObject instanceof RectangleMapObject)) {
            throw new IllegalStateException(
                "Unsupported level object:" + mapObject.getName());
        }

        Rectangle rectangle =
            ((RectangleMapObject) mapObject).getRectangle();


        String type =
            mapObject.getProperties()
                .get("type", String.class);

        if (type == null) {
            throw new IllegalStateException(
                "Map object '"
                    + mapObject.getName()
                    + "' has no type property.");
        }

        ObjectMap<String, Object> properties =
            new ObjectMap<>();

        for (Iterator<String> it
             = mapObject.getProperties().getKeys();
             it.hasNext(); ) {
            String key = it.next();

            if (!key.equals("type")) {
                properties.put(
                    key,
                    mapObject.getProperties().get(key)
                );
            }
        }


        return new LevelObjectData(
            mapObject.getName(),
            type,
            new Vector2(rectangle.x, rectangle.y),
            new Vector2(rectangle.width, rectangle.height),
            properties
        );
    }

}
