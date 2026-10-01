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

        System.out.println("Item pickup created " + itemPickup.item);
        return pickup;
    }
}
