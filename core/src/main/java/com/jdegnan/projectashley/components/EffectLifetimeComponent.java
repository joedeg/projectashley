package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class EffectLifetimeComponent implements Component, Pool.Poolable {

    public float remainingTime;
    @Override
    public void reset() {
        remainingTime = 0f;
    }
}
