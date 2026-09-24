package com.jdegnan.projectashley.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RenderContext {

    private final OrthographicCamera camera;

    private final Viewport viewport;

    private final SpriteBatch spriteBatch;

    private final ShapeRenderer shapeRenderer;

    private final RenderQueue renderQueue;

    public RenderContext(
        OrthographicCamera camera,
        Viewport viewport,
        SpriteBatch spriteBatch,
        ShapeRenderer shapeRenderer) {

        this(camera, viewport, new RenderQueue(), spriteBatch, shapeRenderer);
    }

    public RenderContext(
        OrthographicCamera camera,
        Viewport viewport,
        RenderQueue renderQueue,
        SpriteBatch spriteBatch,
        ShapeRenderer shapeRenderer) {

        this.camera = camera;
        this.viewport = viewport;
        this.renderQueue = renderQueue;
        this.spriteBatch = spriteBatch;
        this.shapeRenderer = shapeRenderer;
    }

    public OrthographicCamera getCamera(){
        return camera;
    }

    public Viewport getViewport(){
        return viewport;
    }

    public SpriteBatch getSpriteBatch(){
        return spriteBatch;
    }

    public ShapeRenderer getShapeRenderer(){
        return shapeRenderer;
    }

    public RenderQueue getRenderQueue(){
        return renderQueue;
    }


}
