package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.ChestAssets;
import com.jdegnan.projectashley.components.ChestComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.level.LevelEntityFactory;
import com.jdegnan.projectashley.level.LevelObjectData;

public class ChestFactory implements LevelEntityFactory {

    private final PooledEngine engine;

    private final Assets assets;



    public ChestFactory(PooledEngine engine, Assets assets) {
        this.engine = engine;
        this.assets = assets;
    }

    @Override
    public Entity create(LevelObjectData object) {

        if(!assets.isLoaded(ChestAssets.CHEST_ATLAS)){
            throw new IllegalStateException("Chest atlas not loaded");
        }

        TextureAtlas atlas = assets.get(ChestAssets.CHEST_ATLAS);

        Entity chest = engine.createEntity();

        PositionComponent positionComponent
            = engine.createComponent(
                PositionComponent.class
        );

        positionComponent.x = object.getPosition().x;
        positionComponent.y = object.getPosition().y;

        ChestComponent chestComponent =
            engine.createComponent(
                ChestComponent.class
            );

        chestComponent.locked =
            object.getBooleanProperty(
                "locked",
                false
            );

        RenderComponent renderComponent =
            engine.createComponent(
                RenderComponent.class
            );
        renderComponent.region =
            atlas.findRegion("blue_chest");

        if(renderComponent.region == null){
            throw new RuntimeException(
                "Could not find region for chest");
        }

        renderComponent.width = 32;
        renderComponent.height = 32;
        renderComponent.layer = 0;

        chest.add(positionComponent);
        chest.add(chestComponent);
        chest.add(renderComponent);

        System.out.println(
            "Created chest at "
                + positionComponent.x
                + ", "
                + positionComponent.y
                + " locked="
                + chestComponent.locked
        );

        return chest;
    }
}
