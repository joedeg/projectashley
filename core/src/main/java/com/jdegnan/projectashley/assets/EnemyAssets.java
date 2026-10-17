package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

public class EnemyAssets {
    public static final AssetDescriptor<Texture> SLIME_IDLE =
        new AssetDescriptor<>(
            "sprites/slime/slimeIdle.png",
            Texture.class
        );

    public static final AssetDescriptor<Texture> SLIME_DYING =
        new AssetDescriptor<>(
            "sprites/slime/slimeDying.png",
            Texture.class
        );

    private EnemyAssets() {
    }
}
