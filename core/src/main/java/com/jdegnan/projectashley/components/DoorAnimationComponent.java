package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class DoorAnimationComponent implements Component, Pool.Poolable {

    public TextureRegion closedFrame;
    public TextureRegion openFrame;

    @Override
    public void reset() {
        closedFrame = null;
        openFrame = null;
    }
}
