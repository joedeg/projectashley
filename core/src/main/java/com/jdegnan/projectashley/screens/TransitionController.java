package com.jdegnan.projectashley.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

public class TransitionController implements Disposable {

    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;

    private float alpha = 0f;
    private float duration = 0.5f;
    private float elapsed = 0f;

    private boolean fadingOut = false;
    private boolean fadingIn = false;

    public TransitionController(OrthographicCamera camera) {
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void fadeOut() {
        elapsed = 0f;
        alpha = 0;
        fadingOut = true;
        fadingIn = false;
    }

    public void fadeIn() {
        elapsed = 0f;
        alpha = 0;
        fadingOut = false;
        fadingIn = true;
    }

    public void update(float deltaTime) {
        if (!fadingIn && !fadingOut) {
            return;
        }

        elapsed += deltaTime;
        float progress = Math.min(elapsed / duration, 1f);

        if (fadingOut) {
            alpha = progress;

            if (progress >= 1f) {
                fadingOut = false;
            }
        } else {
            alpha = 1f - progress;

            if (progress >= 1f) {
                fadingIn = false;
            }
        }
    }

    public void render() {
        if (alpha <= 0f) {
            return;
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA
        );

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, alpha);
        shapeRenderer.rect(
            camera.position.x - camera.viewportWidth * camera.zoom / 2f,
            camera.position.y - camera.viewportHeight * camera.zoom / 2f,
            camera.viewportWidth * camera.zoom,
            camera.viewportHeight * camera.zoom
        );

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public boolean isFadingIn() {
        return fadingIn;
    }

    public boolean isFadingOut() {
        return fadingOut;
    }

    public boolean isFullyFadedOut() {
        return !fadingOut && alpha >=1f;
    }


    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
