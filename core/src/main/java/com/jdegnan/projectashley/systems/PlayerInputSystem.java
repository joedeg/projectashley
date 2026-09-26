package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.jdegnan.projectashley.Direction;
import com.jdegnan.projectashley.components.FacingComponent;
import com.jdegnan.projectashley.components.InteractionRequestComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.components.VelocityComponent;

public class PlayerInputSystem extends IteratingSystem {

    private ComponentMapper<VelocityComponent> vm =
        ComponentMapper.getFor(VelocityComponent.class);

    private ComponentMapper<InteractionRequestComponent> im =
        ComponentMapper.getFor(InteractionRequestComponent.class);

    private ComponentMapper<FacingComponent> fm =
        ComponentMapper.getFor(FacingComponent.class);



    public PlayerInputSystem() {
        super(Family.all(
            PlayerComponent.class,
            VelocityComponent.class,
            InteractionRequestComponent.class,
            FacingComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent vel = vm.get(entity);

        InteractionRequestComponent interaction = im.get(entity);

        FacingComponent facing = fm.get(entity);

        interaction.interact = false;


        vel.vx = 0;
        vel.vy = 0;

        float speed = 200;

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            vel.vx = -speed;
            facing.direction = Direction.LEFT;

        }
         if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            vel.vx = speed;
            facing.direction = Direction.RIGHT;

        }
         if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            vel.vy = speed;
            facing.direction = Direction.UP;

        }
         if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            vel.vy = -speed;
            facing.direction = Direction.DOWN;
        }

         if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            interaction.interact = true;
        }





    }
}
