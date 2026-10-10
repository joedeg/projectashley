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


public class SwordSwingFactory {

    private static final float FRAME_DURATION = 0.05f;
    private static final float EFFECT_SIZE = 32f;

    private final PooledEngine engine;
    private final TextureRegion[] frames;

    public SwordSwingFactory
        (PooledEngine engine,
         SwordSwingAnimation animation) {
        this.engine = engine;
        this.frames = animation.getFrames();
    }

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
        render.width = EFFECT_SIZE;
        render.height = EFFECT_SIZE;
        render.originX = EFFECT_SIZE / 2f;
        render.originY = EFFECT_SIZE / 2f;
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

    private float getRotation(Direction direction) {
        switch (direction) {
            case UP:
                return 0f;
            case DOWN:
                return 180f;
            case LEFT:
                return 90f;
            case RIGHT:
                return -90f;
            default:  // default = UP
                return 0f;
        }
    }
}
