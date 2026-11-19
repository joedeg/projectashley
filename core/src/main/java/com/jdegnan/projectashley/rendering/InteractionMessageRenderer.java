package com.jdegnan.projectashley.rendering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jdegnan.projectashley.components.InteractionMessageComponent;

public class InteractionMessageRenderer {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;
    private final GlyphLayout layout;

    public InteractionMessageRenderer(
        SpriteBatch batch) {
        this.batch = batch;
        this.camera = new OrthographicCamera();
        this.font = new BitmapFont();
        this.shapeRenderer = new ShapeRenderer();
        this.layout = new GlyphLayout();
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

        layout.setText(font, message.message);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 1f, 1f);
        shapeRenderer.rect(50, 20, layout.width + 20, 30);
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, message.message, 60, 40);
        batch.end();
    }


    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }
}
