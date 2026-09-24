package com.jdegnan.projectashley.assets.animations;

/**
 * Represents the various states an actor can be in that dictate which animation should be played.
 * <p>
 * This include directional movement states (e.g., {@code WALK_NORTH}) and general states
 * (e.g., {@code ATTACK}).
 * </p>
 */
public enum AnimationState {
    IDLE_NORTH,
    IDLE_SOUTH,
    IDLE_EAST,
    IDLE_WEST,
    WALK_NORTH,
    WALK_SOUTH,
    WALK_EAST,
    WALK_WEST,
    RUN_NORTH,
    RUN_SOUTH,
    RUN_EAST,
    RUN_WEST,
    IDLE,
    WALK,
    RUN,
    ATTACK,
    HURT,
    DIE
}
