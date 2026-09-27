package com.jdegnan.projectashley.assets.animations;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import com.jdegnan.projectashley.inventory.ItemType;

public class ItemAssetRegistry {

    private final ObjectMap<
        ItemType,
        AssetDescriptor<Texture>>
        assets = new ObjectMap<>();

    public void register(
        ItemType type,
        AssetDescriptor<Texture> descriptor) {

        if (type == null) {
            throw new IllegalArgumentException(
                "type cannot be null."
            );
        }
        if (descriptor == null) {
            throw new IllegalArgumentException(
                "descriptor cannot be null."
            );
        }

        assets.put(type, descriptor);
    }
    public AssetDescriptor<Texture> get(ItemType item) {

        AssetDescriptor<Texture> descriptor = assets.get(item);

        if(descriptor == null){
            throw new IllegalStateException(
                "No asset registered for type:" + item
            );
        }
        return descriptor;
    }

}
