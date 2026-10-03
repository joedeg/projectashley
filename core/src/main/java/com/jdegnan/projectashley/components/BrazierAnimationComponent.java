package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class BrazierAnimationComponent implements Component, Pool.Poolable{

    public  TextureRegion unlitFrame;
    public  TextureRegion[] litFrames;

    public int currentFrame;
    public float timer;
    public boolean playing;


    @Override
    public void reset() {
        unlitFrame = null;
        litFrames = null;
        currentFrame = 0;
        timer = 0;
        playing = false;
    }
}
