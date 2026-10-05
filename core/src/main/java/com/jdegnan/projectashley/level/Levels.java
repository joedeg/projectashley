package com.jdegnan.projectashley.level;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.jdegnan.projectashley.assets.BrazierAssets;
import com.jdegnan.projectashley.assets.ChestAssets;
import com.jdegnan.projectashley.assets.DoorAssets;
import com.jdegnan.projectashley.assets.ItemAssets;
import com.jdegnan.projectashley.assets.PuzzleAssets;

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
        Array.with(
            ChestAssets.CHEST_ATLAS,
            DoorAssets.DOOR_ATLAS,
            ItemAssets.FOREST_KEY,
            ItemAssets.MARSH_AMULET,
            ItemAssets.FIRE_ROD,
            BrazierAssets.BRAZIER,
            PuzzleAssets.GATE_OPEN
            ),
        new Vector2(160, 160)
    );

    private Levels() {

    }
}
