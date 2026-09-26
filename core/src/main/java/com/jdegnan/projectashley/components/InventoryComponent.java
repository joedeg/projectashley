package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.jdegnan.projectashley.inventory.ItemType;

public class InventoryComponent implements Component, Pool.Poolable {

    public ObjectMap<ItemType, Integer> items = new ObjectMap<>();

    public void add(ItemType item){
        int count = items.get(item, 0);

        items.put(item, count + 1);
    }

    public boolean has(ItemType item){
        return items.containsKey(item);
    }

    public int getCount(ItemType item){
        return items.get(item, 0);
    }

    @Override
    public void reset() {
        items.clear();
    }
}
