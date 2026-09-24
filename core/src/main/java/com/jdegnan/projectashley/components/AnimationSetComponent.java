package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;

import com.jdegnan.projectashley.assets.animations.AnimationSet;

/**
 * Component that holds the {@link AnimationSet} for an entity.
 * <p>
 * This component provides the collection of available animations that the entity
 * can transition between.
 * </p>
 */
public class AnimationSetComponent implements Component {

    /**
     * The set of animations available for this entity.
     */
    public AnimationSet set;
}
