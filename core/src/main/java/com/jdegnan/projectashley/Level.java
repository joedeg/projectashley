package com.jdegnan.projectashley;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.jdegnan.projectashley.config.GameConfig;

public class Level {

    private final LevelDefinition definition;
    private final TiledMap map;

    private final MapCollisionData collisionData;
    private final Rectangle mapBounds;

    public Level(
        LevelDefinition definition,
        TiledMap map,
        GameConfig config
    ) {
        this.map = map;

        this.definition = definition;

        this.collisionData =
            new MapCollisionData(
                map,
                config.getTileSize());

        this.mapBounds = collisionData.getMapBounds();
    }

    public LevelDefinition getDefinition() {
        return definition;
    }

    public TiledMap getMap() {
        return map;
    }

    public MapCollisionData getCollisionData() {
        return collisionData;
    }

    public Rectangle getMapBounds() {
        return mapBounds;
    }
}
