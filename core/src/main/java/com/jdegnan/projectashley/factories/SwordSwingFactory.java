package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jdegnan.projectashley.Direction;
import com.jdegnan.projectashley.assets.animations.SwordSwingAnimation;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.SwordSwingComponent;
import com.jdegnan.projectashley.components.EffectLifetimeComponent;


/**
 * Creates the short-lived, directional sword-swing visual entities.
 *
 * <p>The animation frames are shared from {@link SwordSwingAnimation}; the
 * {@code SwordSwingSystem} advances each created effect.</p>
 */
public class SwordSwingFactory {

    private static final float FRAME_DURATION = 0.02f;
    private static final float EFFECT_WIDTH = 32f;
    private static final float EFFECT_HEIGHT = 48f;

    private final PooledEngine engine;
    private final TextureRegion[] frames;

    /**
     * Creates a factory using the engine that will own effects and the loaded animation.
     *
     * @param engine Ashley engine used to create and register effect entities
     * @param animation animation frames used by each swing
     */
    public SwordSwingFactory
        (PooledEngine engine,
         SwordSwingAnimation animation) {
        this.engine = engine;
        this.frames = animation.getFrames();
    }

    /**
     * Creates and adds a swing effect attached to an owner's position.
     *
     * @param owner entity whose movement the visual follows
     * @param x initial world-space x position of the effect
     * @param y initial world-space y position of the effect
     * @param layer render layer for the effect
     * @param direction facing direction used to rotate the animation
     * @return the registered effect entity
     */
    public Entity create(
        Entity owner,
        float x,
        float y,
        int layer,
        Direction direction) {

        Entity effect = engine.createEntity();

        PositionComponent pos =
            engine.createComponent(PositionComponent.class);
        pos.x = x;
        pos.y = y;

        PositionComponent ownerPos =
            owner.getComponent(PositionComponent.class);

        RenderComponent render =
            engine.createComponent(RenderComponent.class);
        render.region = frames[0];
        render.width = EFFECT_WIDTH;
        render.height = EFFECT_HEIGHT;
        render.originX = EFFECT_WIDTH / 2f;
        render.originY = EFFECT_HEIGHT / 2f;
        render.layer = layer;
        render.rotation = getRotation(direction);

        SwordSwingComponent swing =
            engine.createComponent(SwordSwingComponent.class);
        swing.frames = frames;
        swing.frameDuration = FRAME_DURATION;
        swing.stateTime = 0f;

        swing.owner = owner;
        swing.offsetX = x - ownerPos.x;
        swing.offsetY = y - ownerPos.y;


        EffectLifetimeComponent lifetime =
            engine.createComponent(EffectLifetimeComponent.class);
        lifetime.remainingTime = frames.length * FRAME_DURATION;

        effect.add(pos);
        effect.add(render);
        effect.add(swing);
        effect.add(lifetime);

        engine.addEntity(effect);
        return effect;
    }

    /** Maps a facing direction to the sprite rotation used for that attack. */
    private float getRotation(Direction direction) {
        switch (direction) {
            case UP:
                return 90f;
            case DOWN:
                return 270f;
            case LEFT:
                return 180f;
            case RIGHT:
                return -0f;
            default:  // default = UP
                return 0f;
        }
    }
}
