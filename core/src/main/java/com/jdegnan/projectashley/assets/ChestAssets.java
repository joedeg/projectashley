package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ChestAssets {
    public static final AssetDescriptor<TextureAtlas> CHEST_ATLAS =
        new AssetDescriptor<>(
            "sprites/chestAtlas.atlas",
            TextureAtlas.class
        );

    private ChestAssets(){

    }
}
