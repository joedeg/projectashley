package com.jdegnan.projectashley.assets.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;

/**
 * A collection of animations for a single actor, mapped by {@link AnimationState}.
 * <p>
 * This class groups all directional and action-based animations (like walking, idling, etc.)
 * for a specific type of entity.
 * </p>
 */
public class AnimationSet {

    private final EnumMap<
        AnimationState,
        Animation<TextureRegion>>
        animations =
        new EnumMap<>(AnimationState.class);

    /**
     * Retrieves an animation for a specific state.
     *
     * @param state The animation state.
     * @return The associated {@link Animation}, or {@code null} if not found.
     */
    public Animation<TextureRegion> get(AnimationState state) {

        return animations.get(state);
    }

    /**
     * Adds or replaces an animation for a given state.
     *
     * @param state     The animation state.
     * @param animation The animation to associate with the state.
     */
    public void add(
        AnimationState state,
        Animation<TextureRegion> animation) {

        animations.put(state, animation);
    }

    /**
     * Checks if this set contains an animation for the given state.
     *
     * @param state The animation state to check.
     * @return {@code true} if the state exists in this set, {@code false} otherwise.
     */
    public boolean contains(AnimationState state) {
        return animations.containsKey(state);
    }

    /**
     * Returns the internal map of animations.
     *
     * @return An {@link EnumMap} of states to animations.
     */
    public EnumMap<
        AnimationState,
        Animation<TextureRegion>> getAnimations() {
        return animations;
    }
}
