package com.jdegnan.projectashley.save;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.ObjectMap;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.InventoryComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.inventory.ItemType;
import com.jdegnan.projectashley.level.LevelManager;
import com.jdegnan.projectashley.level.LevelRuntime;

public class SaveGameSnapshotFactory {

    private final CollectedPickupTracker collectedPickupTracker;

    public SaveGameSnapshotFactory(CollectedPickupTracker collectedPickupTracker){
        this.collectedPickupTracker = collectedPickupTracker;
    }

    public SaveGameData create(LevelManager levelManager){
        LevelRuntime runtime = levelManager.getCurrentRuntime();
        Entity player = levelManager.getPlayer();

        if(runtime == null || player == null){
            throw new IllegalStateException(
                "Cannot save: no active level or player."
            );
        }

        PositionComponent pos =
            player.getComponent(PositionComponent.class);
        HealthComponent health =
            player.getComponent(HealthComponent.class);
        InventoryComponent inventory =
            player.getComponent(InventoryComponent.class);

        if(pos == null || health == null || inventory == null){
            throw new IllegalStateException(
                "Cannot save: missing player components.");
        }

        SaveGameData data = new SaveGameData();
        data.levelId = runtime.getDefinition().getId();
        data.playerX = pos.x;
        data.playerY = pos.y;
        data.playerHealth = health.hp;
        data.playerMaxHealth = health.maxHp;
        data.collectedPickupIds.addAll(
            collectedPickupTracker.getCollectedIds()
        );
        data.openedDoorIds.addAll(
            levelManager.getOpenedDoorIds()
        );


        for(ObjectMap.Entry<ItemType, Integer> entry : inventory.items){
            data.inventory.put(entry.key.name(), entry.value);
        }

        return data;

    }
}
