package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.jdegnan.projectashley.inventory.ItemType;

public class ChestComponent implements Component, Pool.Poolable{
    public boolean locked;
    public boolean opened;

    public ItemType item;

    @Override
    public void reset() {
        locked = false;
        opened = false;
        item = null;

    }
}
