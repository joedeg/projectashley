package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class RenderComponent implements Component, Pool.Poolable {

    public TextureRegion region;

    public float width;

    public float height;

    public float offsetX;

    public float offsetY;

    public int layer;

    @Override
    public void reset() {
        region = null;
        width = 0;
        height = 0;
        offsetX = 0;
        offsetY = 0;
        layer = 0;
    }
}
