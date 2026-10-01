package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.jdegnan.projectashley.inventory.ItemType;

public class InventoryUIComponent implements Component, Pool.Poolable {

    public boolean visible;
    public ItemType selectedItem;

    @Override
    public void reset() {
        visible = false;
        selectedItem = null;
    }
}
