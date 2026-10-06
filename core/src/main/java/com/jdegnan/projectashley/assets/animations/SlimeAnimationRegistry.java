package com.jdegnan.projectashley.assets.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.EnemyAssets;

public class SlimeAnimationRegistry {

    public AnimationSet create(Assets assets){
        Texture texture = assets.get(EnemyAssets.SLIME_IDLE);
        TextureRegion[][] frames =
            TextureRegion.split(texture, 27, 16);

        Animation<TextureRegion> idleAnimation =
            new Animation<>(
                0.25f,
                frames[0][0],
                frames[0][1]);

        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);

        AnimationSet animationSet = new AnimationSet();
        animationSet.add(
            AnimationState.IDLE,
            idleAnimation
        );

        return animationSet;
    }
}
