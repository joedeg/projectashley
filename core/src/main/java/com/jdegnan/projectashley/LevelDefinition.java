package com.jdegnan.projectashley;

import com.badlogic.gdx.assets.AssetDescriptor;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelDefinition {

    private final String id;
    private final AssetDescriptor<TiledMap> map;
    private final Array<AssetDescriptor<?>> additionalAssets;
    private final Vector2 playerSpawn;

    public LevelDefinition(
        String id,
        AssetDescriptor<TiledMap> map,
        Array<AssetDescriptor<?>> additionalAssets, Vector2 playerSpawn) {

        this.id = id;
        this.map = map;
        this.additionalAssets = additionalAssets;
        this.playerSpawn = new Vector2(playerSpawn);
    }

    public String getId() {
        return id;
    }

    public AssetDescriptor<TiledMap> getMap() {
        return map;
    }

    public Array<AssetDescriptor<?>> getAdditionalAssets() {
        return additionalAssets;
    }

    public Vector2 getPlayerSpawn(){
        return playerSpawn;
    }
}
