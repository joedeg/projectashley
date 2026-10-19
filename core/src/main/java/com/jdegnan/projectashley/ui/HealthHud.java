package com.jdegnan.projectashley.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HealthHud {

    private static final int HEART_SIZE = 32;
    private static final int FRAME_SIZE = 32;
    private static final int HEART_SPACING = 2;

    private final TextureRegion fullHeart;
    private final TextureRegion emptyHeart;

    public HealthHud(Texture heartTexture){
        TextureRegion heartSheet = new TextureRegion(heartTexture);

        fullHeart = new TextureRegion(heartSheet, 0, 0, FRAME_SIZE, FRAME_SIZE);

        emptyHeart = new TextureRegion(heartSheet, FRAME_SIZE, 0, FRAME_SIZE, FRAME_SIZE);
    }

    public void render(SpriteBatch batch, int currentHp, int maxHp){
        int hp = Math.max(0, Math.min(currentHp, maxHp));

        float startX = 8f;
        float startY = 400f - HEART_SIZE - 8f;

        for(int i = 0; i < maxHp; i++){
            TextureRegion heart = i < hp ? fullHeart : emptyHeart;

            float x = startX + i * (HEART_SIZE + HEART_SPACING);

            batch.draw(heart, x, startY, HEART_SIZE, HEART_SIZE);
        }
    }
}
