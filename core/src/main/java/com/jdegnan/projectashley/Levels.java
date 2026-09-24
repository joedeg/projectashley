package com.jdegnan.projectashley;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public final class Levels {

    public static final LevelDefinition FOREST = new LevelDefinition(
        "forest",
        new AssetDescriptor<>(
            "maps/forest.tmx",
            TiledMap.class
        ),
        new Array<>(),
        new Vector2(0, 0)
    );

    public static final LevelDefinition
        DUNGEON =
        new LevelDefinition(
            "dungeon",
            new AssetDescriptor<>(
                "maps/test.tmx",
                TiledMap.class
            ),
            new Array<>(),
            new Vector2(0, 0)
        );

    public static final LevelDefinition ADVENTURE = new LevelDefinition(
        "adventure",
        new AssetDescriptor<>(
            "maps/TopDownAdventure/Adventure.tmx", TiledMap.class
        ),
        new Array<>(),
        new Vector2(160, 160)
    );

    private Levels() {

    }
}
