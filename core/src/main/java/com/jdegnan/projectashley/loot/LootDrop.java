package com.jdegnan.projectashley.loot;

import com.jdegnan.projectashley.inventory.ItemType;

public class LootDrop {

    private final ItemType item;
    private final float weight;

    public LootDrop(ItemType itemType, float weight) {
        if(weight < 0f){
            throw new IllegalArgumentException(
                "Weight cannot be negative."
            );
        }
        this.item = itemType;
        this.weight = weight;
    }

    public ItemType getItem() {
        return item;
    }

    public float getWeight() {
        return weight;
    }
}
