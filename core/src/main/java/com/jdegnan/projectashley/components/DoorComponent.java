package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.jdegnan.projectashley.inventory.ItemType;

public class DoorComponent implements Component, Pool.Poolable{
    public String id;
    public boolean locked;
    public ItemType requiredItem;
    public boolean opened;

    @Override
    public void reset() {
        id = null;
        locked = false;
        requiredItem = null;
        opened = false;
    }
}
