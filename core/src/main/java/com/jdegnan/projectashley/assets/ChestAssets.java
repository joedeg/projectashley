package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ChestAssets {
    public static final AssetDescriptor<TextureAtlas> CHEST_ATLAS =
        new AssetDescriptor<>(
            "sprites/chestAtlas.atlas",
            TextureAtlas.class
        );

    public static final AssetDescriptor<Texture> BROWN_CHEST =
        new AssetDescriptor<>(
            "sprites/brownChest.png",
            Texture.class
        );

    private ChestAssets(){

    }
}
