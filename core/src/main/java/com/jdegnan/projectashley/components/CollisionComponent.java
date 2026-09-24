package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class CollisionComponent implements Component, Pool.Poolable {

    public Entity other;

    @Override
    public void reset() {
        other = null;
    }
}
