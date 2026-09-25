package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jdegnan.projectashley.assets.ActorType;
import com.jdegnan.projectashley.assets.animations.AnimationLibrary;
import com.jdegnan.projectashley.assets.animations.AnimationState;
import com.jdegnan.projectashley.components.AnimationStateComponent;
import com.jdegnan.projectashley.components.AnimationSetComponent;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.InteractionRequestComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.SpriteComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.components.VelocityComponent;

public class PlayerFactory {

    private final AnimationLibrary animationLibrary;

    public PlayerFactory(AnimationLibrary animationLibrary){
        this.animationLibrary = animationLibrary;
    }

    public Entity create(float x, float y){

        Entity entity = new Entity();

        PositionComponent pos = new PositionComponent();

        pos.x = x;
        pos.y = y;

        entity.add(pos);

        SpriteComponent sprite = new SpriteComponent();

        entity.add(sprite);


        AnimationStateComponent anim = new AnimationStateComponent();
        anim.state = AnimationState.IDLE;
        anim.stateTime = 0f;
        anim.looping = true;

        entity.add(anim);

        AnimationSetComponent animSet = new AnimationSetComponent();
        animSet.set = animationLibrary.getSet(ActorType.PLAYER);
        entity.add(animSet);

        RenderComponent render = new RenderComponent();
        render.width = 64;
        render.height = 64;
        render.layer = 1;
        entity.add(render);

        InteractionRequestComponent interaction = new InteractionRequestComponent();
        entity.add(interaction);

        VelocityComponent vel = new VelocityComponent();
        entity.add(vel);

        ColliderComponent col = new ColliderComponent();
        col.localBounds.set(0, 0, 32, 32); // Assuming some collider size
        entity.add(col);

        entity.add(new PlayerComponent());

        Animation<TextureRegion> idle = animSet.set.get(AnimationState.IDLE);

        sprite.textureRegion = idle.getKeyFrame(0);
        render.region = sprite.textureRegion;

        return entity;

    }
}
