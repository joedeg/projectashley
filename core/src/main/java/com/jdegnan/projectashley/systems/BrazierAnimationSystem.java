package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.BrazierAnimationComponent;
import com.jdegnan.projectashley.components.BrazierComponent;
import com.jdegnan.projectashley.components.RenderComponent;

public class BrazierAnimationSystem extends IteratingSystem{

    private final ComponentMapper<BrazierAnimationComponent> am =
        ComponentMapper.getFor(BrazierAnimationComponent.class);

    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);


    private final ComponentMapper<BrazierComponent> bm =
        ComponentMapper.getFor(BrazierComponent.class);

    public BrazierAnimationSystem() {
        super(Family.all(
            BrazierComponent.class,
            RenderComponent.class,
            BrazierAnimationComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BrazierAnimationComponent animation = am.get(entity);

        BrazierComponent brazier = bm.get(entity);

        RenderComponent render = rm.get(entity);

        if(!brazier.lit){
            animation.playing = false;
            animation.currentFrame = 0;
            animation.timer = 0;
            render.region = animation.unlitFrame;
            return;

        }

        if(!animation.playing){
            animation.playing = true;
            animation.timer = 0;
            animation.currentFrame = 0;

            render.region = animation.litFrames[0];
        }

        animation.timer += deltaTime;

        if(animation.timer > 0.12f){
            animation.timer -= 0.12f;
            animation.currentFrame++;

            if(animation.currentFrame >= animation.litFrames.length){
                animation.currentFrame = 0;
            }

            render.region = animation.litFrames[animation.currentFrame];
        }
    }
}
