package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class DoorAssets {
    public static final AssetDescriptor<TextureAtlas> DOOR_ATLAS =
        new AssetDescriptor<>(
                "sprites/doors.atlas",
                TextureAtlas.class
        );

    private DoorAssets() {}
}
