package com.jdegnan.projectashley.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class RenderCommand implements Pool.Poolable{

    public TextureRegion region;

    public float x;
    public float y;

    public float width;
    public float height;

    public float originX;
    public float originY;

    public float scaleX = 1f;
    public float scaleY = 1f;

    public float rotation;

    public int layer;

    public float sortY;

    public Color color = new Color(Color.WHITE);

    @Override
    public void reset() {

        region = null;

        x = 0;
        y = 0;

        width = 0;
        height = 0;

        originX = 0;
        originY = 0;

        scaleX = 1f;
        scaleY = 1f;

        rotation = 0;

        layer = 0;

        sortY = 0;

        color.set(Color.WHITE);
    }
}
