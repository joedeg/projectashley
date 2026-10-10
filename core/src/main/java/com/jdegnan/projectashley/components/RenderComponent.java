package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class RenderComponent implements Component, Pool.Poolable {

    public TextureRegion region;

    public float width;

    public float height;

    public float offsetX;

    public float offsetY;

    public float originX;
    public float originY;

    public int layer;
    public float rotation;

    public Color color = new Color(Color.WHITE);



    @Override
    public void reset() {
        region = null;
        width = 0;
        height = 0;
        offsetX = 0;
        offsetY = 0;
        layer = 0;
        rotation = 0f;
        originX = 0f;
        originY = 0f;

        color.set(Color.WHITE);


    }
}
