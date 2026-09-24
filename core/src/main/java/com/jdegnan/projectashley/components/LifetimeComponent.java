package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class LifetimeComponent implements Component, Pool.Poolable {
    public float timer;

    @Override
    public void reset() {
        timer = 0f;
    }
}
