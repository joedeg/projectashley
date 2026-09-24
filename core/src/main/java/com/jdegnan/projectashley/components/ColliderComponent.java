package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class ColliderComponent implements Component, Pool.Poolable {
    public Rectangle localBounds = new Rectangle();

    public boolean solid = true;

    @Override
    public void reset() {
        solid = true;
    }
}
