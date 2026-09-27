package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.jdegnan.projectashley.inventory.ItemType;

public class ItemPickupComponent implements Component, Pool.Poolable{
    public ItemType item;
    @Override
    public void reset() {
        item = null;
    }
}
