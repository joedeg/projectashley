package com.jdegnan.projectashley.assets.animations;

import com.jdegnan.projectashley.assets.ActorType;

import java.util.EnumMap;

/**
 * A central library for storing and retrieving animation sets for different actor types.
 * <p>
 * This class allows registering a {@link AnimationSet} for a specific {@link ActorType},
 * making it easy to share or swap animation data across different entities.
 * </p>
 */
public class AnimationLibrary {
    private final EnumMap<
        ActorType,
        AnimationSet>
        animationSets =
        new EnumMap<>(ActorType.class);

    /**
     * Registers an animation set for a given actor type.
     *
     * @param actor The actor type to register the set for.
     * @param set   The animation set containing the animations.
     */
    public void register(
        ActorType actor,
        AnimationSet set) {

        animationSets.put(actor, set);
    }

    /**
     * Retrieves the animation set associated with an actor type.
     *
     * @param actor The actor type to look up.
     * @return The associated {@link AnimationSet}, or {@code null} if not found.
     */
    public AnimationSet getSet(
        ActorType actor) {

        return animationSets.get(actor);
    }
}
