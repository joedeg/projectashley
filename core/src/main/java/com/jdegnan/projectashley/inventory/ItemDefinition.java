package com.jdegnan.projectashley.inventory;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

public class ItemDefinition {
    private final ItemType type;
    private final String displayName;
    private final String description;
    private final AssetDescriptor<Texture> asset;

    public ItemDefinition(
        ItemType type,
        String displayName,
        String description,
        AssetDescriptor<Texture> asset) {
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.asset = asset;
    }

    public ItemType getType() {
        return type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public AssetDescriptor<Texture> getAsset() {
        return asset;
    }
}
