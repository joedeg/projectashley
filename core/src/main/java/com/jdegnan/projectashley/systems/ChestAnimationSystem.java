package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.ChestAnimationComponent;
import com.jdegnan.projectashley.components.ChestComponent;
import com.jdegnan.projectashley.components.RenderComponent;

public class ChestAnimationSystem extends IteratingSystem {

    private final ComponentMapper<ChestComponent> cm =
        ComponentMapper.getFor(ChestComponent.class);

    private final ComponentMapper<ChestAnimationComponent> am =
        ComponentMapper.getFor(ChestAnimationComponent.class);

    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);

    private static final float FRAME_DURATION = 0.12f;

    public ChestAnimationSystem() {
        super(Family.all(
            ChestComponent.class,
            ChestAnimationComponent.class,
            RenderComponent.class
        ).get());

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ChestComponent chest = cm.get(entity);

        ChestAnimationComponent animation = am.get(entity);

        RenderComponent render = rm.get(entity);



        if(!chest.opened){
            return;
        }

        if(!animation.playing && animation.currentFrame == 0){
            animation.playing = true;
        }

        if(!animation.playing){
            return;
        }

        animation.timer += deltaTime;

        if(animation.timer < FRAME_DURATION){
            return;
        }

        animation.timer -= FRAME_DURATION;

        animation.currentFrame++;

        if(animation.currentFrame >= animation.frames.size) {
            animation.currentFrame = animation.frames.size - 1;

            animation.playing = false;
        }


        render.region =
            animation.frames.get(
                animation.currentFrame
            );



    }
}
