package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ChestAnimationComponent implements Component, Pool.Poolable {

    public Array<TextureRegion> frames = new Array<>();

    public int currentFrame;
    public float timer;

    public boolean playing;
    @Override
    public void reset() {
        frames.clear();
        currentFrame = 0;
        timer = 0;
        playing = false;
    }
}
