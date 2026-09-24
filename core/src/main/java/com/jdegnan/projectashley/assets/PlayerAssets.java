package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class PlayerAssets {

    public static final AssetDescriptor<TextureAtlas> PLAYER_ATLAS =
        new AssetDescriptor<>("sprites/player.atlas",
            TextureAtlas.class);

    private PlayerAssets(){}
}
