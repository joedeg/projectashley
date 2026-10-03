package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

public class ItemAssets {

    public static final AssetDescriptor<Texture> FOREST_KEY =
        new AssetDescriptor<>(
            "sprites/forest_key.png",
            Texture.class
        );

    public static final AssetDescriptor<Texture> MARSH_AMULET =
        new AssetDescriptor<>(
            "sprites/marsh_amulet.png",
            Texture.class
        );

    public static final AssetDescriptor<Texture> FIRE_ROD =
        new AssetDescriptor<>(
            "sprites/fire_rod.png",
            Texture.class
        );




    private ItemAssets(){}
}
