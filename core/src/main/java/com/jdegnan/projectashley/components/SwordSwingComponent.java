package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class SwordSwingComponent implements Component, Pool.Poolable{

    public TextureRegion[] frames;
    public float frameDuration = 0.5f;
    public float stateTime;

    public Entity owner;
    public float offsetX;
    public float offsetY;

    @Override
    public void reset() {
        frames = null;
        frameDuration = 0.05f;
        stateTime = 0f;
        owner = null;
        offsetX = 0f;
        offsetY = 0f;

    }
}
