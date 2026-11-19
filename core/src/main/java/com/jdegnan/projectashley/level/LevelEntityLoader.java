package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;


/**
 * Creates runtime entities from objects in a Tiled map's {@code Entities} layer.
 *
 * <p>Each object must have a {@code type} property registered with the level
 * entity factory registry. Rectangle and texture-backed tile objects are
 * converted to {@link LevelObjectData} before their factory is invoked.</p>
 */
public class LevelEntityLoader {
    private final LevelEntityFactoryRegistry factoryRegistry;

    /**
     * Creates a loader that delegates object construction to the given registry.
     *
     * @param factoryRegistry factories keyed by each map object's {@code type}
     */
    public LevelEntityLoader(
        LevelEntityFactoryRegistry factoryRegistry) {
        this.factoryRegistry = factoryRegistry;
    }

    /**
     * Loads all supported objects from the map's {@code Entities} layer and adds
     * the created entities to the level runtime.
     *
     * @param map the Tiled map to read
     * @param engine the Ashley engine used by the runtime
     * @param runtime the level runtime that owns the created entities
     */
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

    /**
     * Extracts an object's geometry and custom properties for its entity factory.
     *
     * <p>Tile-object dimensions include their Tiled scale. The {@code type}
     * property selects the factory and is omitted from the custom property map.</p>
     *
     * @param mapObject the Tiled map object to convert
     * @return normalized object data for the registered factory
     * @throws IllegalStateException if the object's geometry is unsupported or
     *                               it has no {@code type} property
     */
    private LevelObjectData createObjectData(MapObject mapObject) {
        Vector2 position;
        Vector2 size;

        if (mapObject instanceof RectangleMapObject) {
            Rectangle rectangle =
                ((RectangleMapObject) mapObject).getRectangle();
            position = new Vector2(rectangle.x, rectangle.y);
            size = new Vector2(rectangle.width, rectangle.height);
        } else if (mapObject instanceof TextureMapObject) {
            TextureMapObject tileObject = (TextureMapObject) mapObject;
            position = new Vector2(tileObject.getX(), tileObject.getY());
            size = new Vector2(
                tileObject.getTextureRegion().getRegionWidth() * tileObject.getScaleX(),
                tileObject.getTextureRegion().getRegionHeight() * tileObject.getScaleY()
            );
        } else {
            throw new IllegalStateException(
                "Unsupported level object type: "
                    + mapObject.getClass().getSimpleName()
            );
        }

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
            position,
            size,
            properties
        );
    }

}
