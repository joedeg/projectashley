package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import com.jdegnan.projectashley.inventory.ItemDefinition;
import com.jdegnan.projectashley.inventory.ItemType;

public class ItemAssetRegistry {

    private final ObjectMap<
        ItemType,
        ItemDefinition
        > items = new ObjectMap<>();

    public void register(ItemDefinition definition){
        if(definition == null){
            throw new IllegalArgumentException(
                "definition cannot be null."
            );
        }

        items.put(definition.getType(), definition);
    }

    public ItemDefinition get(ItemType item){
        ItemDefinition definition = items.get(item);

        if(definition == null){
            throw new IllegalStateException(
                "No item definition found for " + item
            );
        }
        return definition;
    }

}
