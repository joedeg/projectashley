package com.jdegnan.projectashley.assets.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Splits the sword-swing sprite sheet into the frames used by combat visuals.
 *
 * <p>The sheet is expected to contain frames stacked vertically in a column,
 * each measuring 32 by 48 pixels.</p>
 */
public class SwordSwingAnimation {

    private final TextureRegion[] frames;

    /**
     * Extracts animation frames from the first column of the sprite sheet.
     *
     * @param texture sprite sheet with 32 by 48 frames arranged vertically in a column
     */
    public SwordSwingAnimation(Texture texture){

        TextureRegion[][] split = TextureRegion.split(texture, 32, 48);

        int numFrames = Math.min(6, split.length);
        frames = new TextureRegion[numFrames];

        for (int i = 0; i < numFrames; i++) {
            frames[i] = split[i][0];
        }
    }

    /**
     * Returns the animation frames in playback order.
     *
     * @return the shared frame array; callers should treat it as read-only
     */
    public TextureRegion[] getFrames(){
        return frames;
    }
}
