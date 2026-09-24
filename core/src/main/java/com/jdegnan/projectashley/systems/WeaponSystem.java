package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.factories.BulletFactory;

public class WeaponSystem extends IteratingSystem {

    private BulletFactory bulletFactory;


    public WeaponSystem(BulletFactory bulletFactory) {
        super(Family.all(PlayerComponent.class).get());

        this.bulletFactory = bulletFactory;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            bulletFactory.create(100, 200, 500, 0);
        }

    }
}
