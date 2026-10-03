package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BrazierComponent implements Component, Pool.Poolable {
    public boolean lit;
    @Override
    public void reset() {
        lit = false;
    }
}
