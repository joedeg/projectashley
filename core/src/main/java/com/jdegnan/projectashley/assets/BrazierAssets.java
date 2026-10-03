package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

public class BrazierAssets {

    public static final AssetDescriptor<Texture> BRAZIER =
        new AssetDescriptor<>(
            "sprites/brazier.png",
            Texture.class
        );

    private BrazierAssets(){}
}
