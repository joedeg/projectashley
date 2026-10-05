package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class GateGlowComponent implements Component, Pool.Poolable {

    public float timer;
    public float duration;
    public float maxRadius;

    public boolean active;

    @Override
    public void reset() {
        timer = 0f;
        duration = 0f;
        maxRadius = 0f;
        active = false;
    }
}

