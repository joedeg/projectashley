package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.jdegnan.projectashley.inventory.ItemType;

public class DoorComponent implements Component, Pool.Poolable{
    public boolean locked;
    public ItemType requiredItem;
    public boolean opened;

    @Override
    public void reset() {
        locked = false;
        requiredItem = null;
        opened = false;
    }
}
