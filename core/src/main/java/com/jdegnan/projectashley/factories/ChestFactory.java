package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.ChestAssets;
import com.jdegnan.projectashley.components.ChestAnimationComponent;
import com.jdegnan.projectashley.components.ChestComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.inventory.ItemType;
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

        if(!assets.isLoaded(ChestAssets.BROWN_CHEST)){
            throw new IllegalStateException("Chest atlas not loaded");
        }



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

        String itemName =
            object.getStringProperty("item", null);

        if(itemName != null){
            chestComponent.item =
                ItemType.valueOf(itemName.toUpperCase());
        }

        RenderComponent renderComponent =
            engine.createComponent(
                RenderComponent.class
            );

        ChestAnimationComponent animationComponent =
            engine.createComponent(
                ChestAnimationComponent.class);

        Texture chest_texture = assets.get(ChestAssets.BROWN_CHEST);

        TextureRegion chest_region = new TextureRegion(chest_texture);
        TextureRegion[][] split =
            chest_region.split(16, 32);

        for (TextureRegion[] textureRegions : split) {
            for (TextureRegion textureRegion : textureRegions) {
                animationComponent.frames.add(textureRegion);
            }
        }

        animationComponent.currentFrame = 0;
        animationComponent.timer = 0;
        animationComponent.playing = false;

        renderComponent.region =
            animationComponent.frames.get(0);

        if(renderComponent.region == null){
            throw new RuntimeException(
                "Could not find region for chest");
        }

        renderComponent.width = renderComponent.region.getRegionWidth();
        renderComponent.height = renderComponent.region.getRegionHeight();
        renderComponent.layer = 0;

        chest.add(animationComponent);
        chest.add(positionComponent);
        chest.add(chestComponent);
        chest.add(renderComponent);


        return chest;
    }
}
