package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.jdegnan.projectashley.components.InteractionRequestComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.components.VelocityComponent;

public class PlayerInputSystem extends IteratingSystem {

    private ComponentMapper<VelocityComponent> vm =
        ComponentMapper.getFor(VelocityComponent.class);

    private ComponentMapper<InteractionRequestComponent> im =
        ComponentMapper.getFor(InteractionRequestComponent.class);


    public PlayerInputSystem() {
        super(Family.all(
            PlayerComponent.class,
            VelocityComponent.class



        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent vel = vm.get(entity);

        InteractionRequestComponent interaction = im.get(entity);

        interaction.interact = false;


        vel.vx = 0;
        vel.vy = 0;

        float speed = 200;

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            vel.vx = -speed;

        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            vel.vx = speed;

        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            vel.vy = speed;

        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            vel.vy = -speed;

        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            interaction.interact = true;
        }

    }
}
