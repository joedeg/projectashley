package com.jdegnan.projectashley.assets.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jdegnan.projectashley.assets.Assets;

public interface AnimationRegistry {
    void register(
        Assets assets,
        AnimationLibrary library);

    void registerDirectionalAnimation(
        AnimationSet set,
        AnimationState state,
        TextureRegion[] frames);
}
