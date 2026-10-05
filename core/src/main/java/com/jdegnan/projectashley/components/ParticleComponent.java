package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ParticleComponent implements Component, Pool.Poolable{

    public float velocityX;
    public float velocityY;

    public float lifetime;
    public float remainingTime;

    public float size;

    public float drag;
    @Override
    public void reset() {
        velocityX = 0;
        velocityY = 0f;
        lifetime = 0f;
        remainingTime = 0f;
        size = 0f;
        drag = 0f;

    }
}
