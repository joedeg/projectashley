package com.jdegnan.projectashley.assets.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SwordSwingAnimation {

    private final TextureRegion[] frames;

    public SwordSwingAnimation(Texture texture){
        TextureRegion[][] split = TextureRegion.split(texture,32,32);

        frames = new TextureRegion[4];

        System.arraycopy(split[0], 0, frames, 0, 4);
    }

    public TextureRegion[] getFrames(){
        return frames;
    }
}
