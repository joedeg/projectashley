package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DamageCooldownComponent implements Component, Pool.Poolable{
    public float remaining;
    @Override
    public void reset() {
        remaining = 0;
    }
}
