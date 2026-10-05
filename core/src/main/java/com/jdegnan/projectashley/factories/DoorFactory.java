package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.DoorAssets;
import com.jdegnan.projectashley.components.BrazierGateComponent;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.DoorAnimationComponent;
import com.jdegnan.projectashley.components.DoorComponent;
import com.jdegnan.projectashley.components.GateGlowComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.TagComponents.WallComponent;
import com.jdegnan.projectashley.inventory.ItemType;
import com.jdegnan.projectashley.level.LevelEntityFactory;
import com.jdegnan.projectashley.level.LevelObjectData;

public class DoorFactory implements LevelEntityFactory {
    private final PooledEngine engine;
    private final Assets assets;

    public DoorFactory(PooledEngine engine, Assets assets) {
        this.engine = engine;
        this.assets = assets;
    }

    @Override
    public Entity create(LevelObjectData object) {

        Entity door = engine.createEntity();

        PositionComponent pos =
            engine.createComponent(PositionComponent.class);

        ColliderComponent collider =
            engine.createComponent(ColliderComponent.class);

        DoorComponent doorComp =
            engine.createComponent(DoorComponent.class);

        WallComponent wallComp =
            engine.createComponent(WallComponent.class);

        RenderComponent render =
            engine.createComponent(RenderComponent.class);

        DoorAnimationComponent animation =
            engine.createComponent(DoorAnimationComponent.class);

        BrazierGateComponent gate = engine.createComponent(BrazierGateComponent.class);

        gate.requiredBraziers =
            object.getIntProperty(
                "requiredBraziers",
                1
            );

        gate.puzzleId = object.getStringProperty("puzzleId", null);

        if (gate.puzzleId != null) {
            BrazierGateComponent gateComp =
                engine.createComponent(BrazierGateComponent.class);

            gateComp.puzzleId = gate.puzzleId;
            gateComp.requiredBraziers =
                object.getIntProperty("requiredBraziers", 1);

            GateGlowComponent glowComp =
                engine.createComponent(GateGlowComponent.class);

            door.add(glowComp);
            door.add(gateComp);
        }

        if (!assets.isLoaded(DoorAssets.DOOR_ATLAS)) {
            throw new IllegalStateException("Door atlas not loaded");
        }

        TextureAtlas atlas = assets.get(DoorAssets.DOOR_ATLAS);

        TextureRegion doorRegion =
            atlas.findRegion("large_gate");

        if (doorRegion == null) {
            throw new IllegalStateException(
                "Door atlas region 'large_gate' not found."
            );
        }

        TextureRegion[][] split = doorRegion.split(32, 96);

        animation.closedFrame = split[0][0];
        animation.openFrame = split[0][1];

        render.region = animation.closedFrame;
        render.width = render.region.getRegionWidth();
        render.height = render.region.getRegionHeight();
        render.layer = 0;


        pos.x = object.getPosition().x;
        pos.y = object.getPosition().y;

        collider.localBounds.set(
            pos.x,
            pos.y,
            object.getSize().x,
            object.getSize().y
        );

        String requiredItem =
            object.getStringProperty("requiredItem", null);

        if (requiredItem != null) {
            doorComp.requiredItem =
                ItemType.valueOf(
                    requiredItem.toUpperCase()
                );
        }

        doorComp.locked = true;
        doorComp.opened = false;


        door.add(pos);
        door.add(collider);
        door.add(doorComp);
        door.add(wallComp);
        door.add(animation);
        door.add(render);

        return door;
    }


}
