package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ChestComponent implements Component, Pool.Poolable{
    public boolean locked;
    public boolean opened;
    @Override
    public void reset() {
        locked = false;
        opened = false;

    }
}
