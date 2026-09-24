package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.jdegnan.projectashley.assets.animations.AnimationState;

/**
 * Component that tracks the current animation state and timing for an entity.
 * <p>
 * This component is used by {@link com.jdegnan.projectashley.systems.AnimationSystem} to determine
 * which frame of an animation should be rendered at any given time.
 * </p>
 */
public class AnimationStateComponent implements Component, Pool.Poolable {

    /**
     * The current active animation. This is typically set by the {@link com.jdegnan.projectashley.systems.AnimationSystem}
     * based on the {@link #state}.
     */
    public Animation<TextureRegion> animation;

    /**
     * The current logical state of the entity (e.g., WALK_NORTH).
     */
    public AnimationState state = AnimationState.IDLE;

    /**
     * The elapsed time since the current state/animation started.
     */
    public float stateTime = 0f;

    /**
     * Whether the animation should loop.
     */
    public boolean looping = true;

    @Override
    public void reset() {
        state = AnimationState.IDLE;
        looping = true;
        stateTime = 0f;
    }
}
