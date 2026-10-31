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




        // WALK UP
        TextureRegion walkRegion = atlas.findRegion("floki_walk_up");
        TextureRegion[][] walkNorth = walkRegion.split(32, 32);
        registerDirectionalAnimation(player, AnimationState.WALK_NORTH, walkNorth[0]);


        // WALK DOWN
        TextureRegion walkDown = atlas.findRegion("floki_walk_down");
        TextureRegion[][] walkSouth = walkDown.split(32, 32);
        registerDirectionalAnimation(player, AnimationState.WALK_SOUTH, walkSouth[0]);

        // WALK LEFT
        TextureRegion walkLeft = atlas.findRegion("floki_walk_left");
        TextureRegion[][] walkWest = walkLeft.split(32, 32);
        registerDirectionalAnimation(player, AnimationState.WALK_WEST, walkWest[0]);

        // WALK RIGHT
        TextureRegion walkRight = atlas.findRegion("floki_walk_right");
        TextureRegion[][] walkEast = walkRight.split(32, 32);
        registerDirectionalAnimation(player, AnimationState.WALK_EAST, walkEast[0]);


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
