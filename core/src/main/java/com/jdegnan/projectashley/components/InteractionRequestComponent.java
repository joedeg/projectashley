package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InteractionRequestComponent implements Component, Pool.Poolable {

    public boolean interact;

    @Override
    public void reset() {
        interact = false;
    }
}
