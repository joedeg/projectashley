package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.assets.animations.AnimationState;
import com.jdegnan.projectashley.components.AnimationStateComponent;
import com.jdegnan.projectashley.components.VelocityComponent;

/**
 * System that updates the {@link AnimationState} of player entities based on their movement.
 * <p>
 * This system reads the {@link VelocityComponent} to determine the direction of movement
 * and updates the {@link AnimationStateComponent} accordingly. When velocity is zero, it
 * transitions to the corresponding idle state.
 * </p>
 */
public class PlayerAnimationStateSystem extends IteratingSystem {

    private ComponentMapper<VelocityComponent> vm =
        ComponentMapper.getFor(VelocityComponent.class);

    private ComponentMapper<AnimationStateComponent> am =
        ComponentMapper.getFor(AnimationStateComponent.class);

    /**
     * Creates a new PlayerAnimationStateSystem.
     */
    public PlayerAnimationStateSystem() {
        super(Family.all(
            VelocityComponent.class,
            AnimationStateComponent.class
        ).get());
    }

    /**
     * Processes a player entity to update its animation state based on velocity.
     *
     * @param entity    The entity being processed.
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent vel = vm.get(entity);
        AnimationStateComponent anim = am.get(entity);

        if (vel.vx > 0) anim.state = AnimationState.WALK_EAST;
        else if (vel.vx < 0) anim.state = AnimationState.WALK_WEST;
        else if (vel.vy > 0) anim.state = AnimationState.WALK_NORTH;
        else if (vel.vy < 0) anim.state = AnimationState.WALK_SOUTH;
        else {
            // Transition to idle states if there is no velocity
            if (anim.state == AnimationState.WALK_EAST)
                anim.state = AnimationState.IDLE_EAST;
            else if (anim.state == AnimationState.WALK_WEST)
                anim.state = AnimationState.IDLE_WEST;
            else if (anim.state == AnimationState.WALK_NORTH)
                anim.state = AnimationState.IDLE_NORTH;
            else if (anim.state == AnimationState.WALK_SOUTH)
                anim.state = AnimationState.IDLE_SOUTH;
        }
    }
}
