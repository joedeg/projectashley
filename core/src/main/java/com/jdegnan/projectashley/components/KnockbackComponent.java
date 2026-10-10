package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class KnockbackComponent implements Component, Pool.Poolable {

    public float velocityX;
    public float velocityY;
    public float remainingTime;
    public boolean active;

    @Override
    public void reset() {
        velocityX = 0f;
        velocityY = 0f;
        remainingTime = 0f;
        active = false;
    }
}
