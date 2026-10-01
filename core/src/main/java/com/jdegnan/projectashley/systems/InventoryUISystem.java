package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.jdegnan.projectashley.components.InventoryUIComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;

public class InventoryUISystem extends IteratingSystem {

    private final ComponentMapper<InventoryUIComponent> im =
        ComponentMapper.getFor(InventoryUIComponent.class);

    public InventoryUISystem() {
        super(Family.all(
            PlayerComponent.class,
            InventoryUIComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InventoryUIComponent ui = im.get(entity);

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            ui.visible = !ui.visible;
        }
    }
}

