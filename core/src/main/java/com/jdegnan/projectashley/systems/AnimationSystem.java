package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jdegnan.projectashley.assets.animations.AnimationState;
import com.jdegnan.projectashley.components.AnimationStateComponent;
import com.jdegnan.projectashley.components.AnimationSetComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.SpriteComponent;

/**
 * System responsible for updating animation timers and applying the current animation frame to an entity's sprite.
 * <p>
 * This system increments the {@code stateTime} in the {@link AnimationStateComponent}, selects the active
 * animation from the {@link AnimationSetComponent} based on the current {@link AnimationState},
 * and finally updates the {@link SpriteComponent}'s texture region.
 * </p>
 */
public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<AnimationStateComponent> am =
        ComponentMapper.getFor(AnimationStateComponent.class);

    private ComponentMapper<AnimationSetComponent> asm =
        ComponentMapper.getFor(AnimationSetComponent.class);

    private ComponentMapper<SpriteComponent> sm =
        ComponentMapper.getFor(SpriteComponent.class);

    private ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);


    /**
     * Creates a new AnimationSystem.
     */
    public AnimationSystem() {
        super(Family.all(
            AnimationStateComponent.class,
            AnimationSetComponent.class,
            SpriteComponent.class
        ).get());
    }

    /**
     * Processes an entity to update its animation state and visual frame.
     *
     * @param entity    The entity being processed.
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationStateComponent anim = am.get(entity);
        AnimationSetComponent set = asm.get(entity);

        SpriteComponent sprite = sm.get(entity);
        RenderComponent render = rm.get(entity);

        // Increment the animation timer
        anim.stateTime += deltaTime;

        Animation<TextureRegion> current = set.set.get(anim.state);

        if (current == null) {
            return;
        }

        sprite.textureRegion = current.getKeyFrame(
            anim.stateTime,
            anim.looping);

        if (render != null) {
            render.region = sprite.textureRegion;
        }

    }
}

