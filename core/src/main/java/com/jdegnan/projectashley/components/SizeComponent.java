package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class SizeComponent implements Component, Pool.Poolable {

    public float width = 0;
    public float height = 0;

    @Override
    public void reset() {
        width = 0;
        height = 0;
    }
}
