package com.jdegnan.projectashley.rendering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jdegnan.projectashley.components.InteractionMessageComponent;

public class InteractionMessageRenderer {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final BitmapFont font;

    public InteractionMessageRenderer(
        SpriteBatch batch) {
        this.batch = batch;
        this.camera = new OrthographicCamera();
        this.font = new BitmapFont();
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    public void render(Entity player) {
        if (player == null) {
            return;
        }

        InteractionMessageComponent message =
            player.getComponent(InteractionMessageComponent.class);

        if (message == null ||
            message.message == null ||
            message.remainingTime <= 0f) {
            return;
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, message.message, 60, 40);
        batch.end();
    }


    public void dispose() {
        font.dispose();
    }
}
