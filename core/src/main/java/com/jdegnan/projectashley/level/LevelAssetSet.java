package com.jdegnan.projectashley.level;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.jdegnan.projectashley.assets.Assets;

public class LevelAssetSet {

    private final Assets assets;
    private final LevelDefinition definition;

    private boolean released;

    public LevelAssetSet(Assets assets, LevelDefinition definition) {
        this.assets = assets;
        this.definition = definition;
    }

    public void release() {
        if (released) {
            return;
        }

        assets.release(definition.getMap());

        for (AssetDescriptor<?> asset : definition.getAdditionalAssets()) {
            assets.release(asset);
        }

        released = true;
    }

    public boolean isReleased() {
        return released;
    }
}
