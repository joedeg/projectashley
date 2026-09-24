package com.jdegnan.projectashley.assets.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jdegnan.projectashley.assets.ActorType;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.PlayerAssets;

/**
 * Registry class responsible for loading and configuring animations for the player character.
 * <p>
 * It splits the player's texture atlas into individual frames and maps them to specific
 * {@link AnimationState} values within an {@link AnimationSet}, which is then stored in the
 * {@link AnimationLibrary}.
 * </p>
 */
public class PlayerAnimationRegistry implements AnimationRegistry {

    /**
     * Registers player animations into the provided library.
     *
     * @param assets  The asset manager containing the player atlas.
     * @param library The animation library to register the player set into.
     */
    @Override
    public void register(
        Assets assets,
        AnimationLibrary library) {

        TextureAtlas atlas = assets.get(PlayerAssets.PLAYER_ATLAS);

        AnimationSet player = new AnimationSet();

        // Assuming 64x64 frames based on atlas bounds (128x256 for idle = 2x4 frames)
        // layout North [0], West [1], South [2], East [3]

        // IDLE
        TextureRegion idleRegion = atlas.findRegion("idle");
        TextureRegion[][] idleFrames = idleRegion.split(64, 64);

        registerDirectionalAnimation(player, AnimationState.IDLE_NORTH, idleFrames[0]);
        registerDirectionalAnimation(player, AnimationState.IDLE_WEST, idleFrames[1]);
        registerDirectionalAnimation(player, AnimationState.IDLE_SOUTH, idleFrames[2]);
        registerDirectionalAnimation(player, AnimationState.IDLE_EAST, idleFrames[3]);


        // WALK
        TextureRegion walkRegion = atlas.findRegion("walk");
        TextureRegion[][] walkFrames = walkRegion.split(64, 64);

        registerDirectionalAnimation(player, AnimationState.WALK_NORTH, walkFrames[0]);
        registerDirectionalAnimation(player, AnimationState.WALK_WEST, walkFrames[1]);
        registerDirectionalAnimation(player, AnimationState.WALK_SOUTH, walkFrames[2]);
        registerDirectionalAnimation(player, AnimationState.WALK_EAST, walkFrames[3]);


        // RUN
        TextureRegion runRegion = atlas.findRegion("run");
        TextureRegion[][] runFrames = runRegion.split(64, 64);

        registerDirectionalAnimation(player, AnimationState.RUN_NORTH, runFrames[0]);
        registerDirectionalAnimation(player, AnimationState.RUN_WEST, runFrames[1]);
        registerDirectionalAnimation(player, AnimationState.RUN_SOUTH, runFrames[2]);
        registerDirectionalAnimation(player, AnimationState.RUN_EAST, runFrames[3]);


        // Backwards compatibility for general states
        player.add(AnimationState.IDLE, player.get(AnimationState.IDLE_SOUTH));
        player.add(AnimationState.WALK, player.get(AnimationState.WALK_SOUTH));
        player.add(AnimationState.RUN, player.get(AnimationState.RUN_SOUTH));

        library.register(ActorType.PLAYER, player);
    }

    /**
     * Helper method to create and register a looping animation.
     *
     * @param set    The animation set to add the animation to.
     * @param state  The state this animation represents.
     * @param frames The frames for the animation.
     */
    @Override
    public void registerDirectionalAnimation(AnimationSet set, AnimationState state, TextureRegion[] frames) {
        Animation<TextureRegion> anim = new Animation<>(0.2f, frames);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        set.add(state, anim);
    }
}
