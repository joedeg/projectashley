package com.jdegnan.projectashley.level;

import com.badlogic.gdx.assets.AssetDescriptor;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.audio.Music;

public class LevelDefinition {

    private final String id;
    private final AssetDescriptor<TiledMap> map;
    private final Array<AssetDescriptor<?>> additionalAssets;
    private final Vector2 playerSpawn;

    private final AssetDescriptor<Music> music;

    public LevelDefinition(
        String id,
        AssetDescriptor<TiledMap> map,
        Array<AssetDescriptor<?>> additionalAssets,
        Vector2 playerSpawn,
        AssetDescriptor<Music> music) {

        this.id = id;
        this.map = map;
        this.additionalAssets = additionalAssets;
        this.playerSpawn = new Vector2(playerSpawn);
        this.music = music;
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

    public AssetDescriptor<Music> getMusic() {
        return music;
    }
}
