package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class BulletAssets {
    public static final AssetDescriptor<TextureAtlas> BULLET_ATLAS =
        new AssetDescriptor<>(
            "sprites/bullets.atlas",
            TextureAtlas.class
        );

    private BulletAssets() {
    }
}
