package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.ItemAssetRegistry;
import com.jdegnan.projectashley.components.ItemPickupComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.inventory.ItemDefinition;
import com.jdegnan.projectashley.inventory.ItemType;
import com.jdegnan.projectashley.level.LevelEntityFactory;
import com.jdegnan.projectashley.level.LevelObjectData;

import java.util.UUID;


public class ItemPickupFactory implements LevelEntityFactory {

    private final PooledEngine engine;
    private final Assets assets;

    private final ItemAssetRegistry itemAssetRegistry;

    public ItemPickupFactory(
        PooledEngine engine,
        Assets assets,
        ItemAssetRegistry itemAssetRegistry) {
        this.engine = engine;
        this.assets = assets;
        this.itemAssetRegistry = itemAssetRegistry;
    }

    @Override
    public Entity create(LevelObjectData object) {
        Entity pickup = engine.createEntity();

        PositionComponent pos =
            engine.createComponent(PositionComponent.class);

        ItemPickupComponent itemPickup =
            engine.createComponent(ItemPickupComponent.class);

        RenderComponent render =
            engine.createComponent(RenderComponent.class);

        String itemName =
            object.getStringProperty("item", null);

        if (itemName == null) {
            throw new IllegalStateException(
                "Item pickup object" +
                    object.getName() +
                    "missing 'item' property"
            );
        }

        itemPickup.item =
            ItemType.valueOf(itemName.toUpperCase());

        itemPickup.id = object.getStringProperty("id", null);

        if (itemPickup.id == null || itemPickup.id.isBlank()) {
            throw new IllegalStateException(
                "Item pickup '" + object.getName() + "' missing 'id' property"
            );
        }


        ItemDefinition definition =
            itemAssetRegistry.get(itemPickup.item);

        Texture texture = assets.get(definition.getAsset());

        TextureRegion region = new TextureRegion(texture);

        render.region = region;
        render.width = render.region.getRegionWidth();
        render.height = render.region.getRegionHeight();

        pos.x = object.getPosition().x;
        pos.y = object.getPosition().y;

        pickup.add(render);
        pickup.add(pos);
        pickup.add(itemPickup);


        return pickup;
    }

    public Entity createDrop(ItemType itemType, float x, float y) {
        return createDrop(
            itemType,
            x,
            y,
            "drop_" + UUID.randomUUID()
        );
    }

    public Entity createDrop(
        ItemType itemType,
        float x,
        float y,
        String id
    ) {


        Entity pickup = engine.createEntity();

        PositionComponent pos =
            engine.createComponent(PositionComponent.class);
        pos.x = x;
        pos.y = y;

        ItemPickupComponent itemPickup =
            engine.createComponent(ItemPickupComponent.class);
        itemPickup.item = itemType;
        itemPickup.id = id;

        RenderComponent render =
            engine.createComponent(RenderComponent.class);

        ItemDefinition definition = itemAssetRegistry.get(itemType);

        Texture texture = assets.get(definition.getAsset());

        TextureRegion region;

        if (itemType == ItemType.HEART) {
            region = new TextureRegion(texture, 0, 0, 32, 32);
        } else {
            region = new TextureRegion(texture);
        }

        render.region = region;
        render.width = render.region.getRegionWidth();
        render.height = render.region.getRegionHeight();

        pickup.add(render);
        pickup.add(pos);
        pickup.add(itemPickup);



        engine.addEntity(pickup);


        return pickup;

    }


}
