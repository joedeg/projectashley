package com.jdegnan.projectashley.save;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.ObjectMap;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.InventoryComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.inventory.ItemType;
import com.jdegnan.projectashley.level.LevelManager;

public class SaveGameRestorer {
    public void restorePlayer(
        SaveGameData data,
        LevelManager levelManager){
        if(data == null){
            throw new IllegalArgumentException(
                "Save data cannot be null."

            );
        }

        Entity player = levelManager.getPlayer();

        if(player == null){
            throw new IllegalStateException(
                "Cannot restore player: no player entity found."
            );
        }

        PositionComponent position =
            player.getComponent(PositionComponent.class);

        HealthComponent health =
            player.getComponent(HealthComponent.class);

        InventoryComponent inventory =
            player.getComponent(InventoryComponent.class);

        if(position == null || health == null || inventory == null){
            throw new IllegalStateException(
                "Cannot restore player: missing player components."
            );
        }

        position.x = data.playerX;
        position.y = data.playerY;

        health.maxHp = data.playerMaxHealth;
        health.hp = Math.max(0, Math.min(data.playerHealth, health.maxHp));

        inventory.items.clear();

        if(data.inventory != null){
            for(ObjectMap.Entry<String, Integer> entry : data.inventory){
                try{
                    ItemType itemType = ItemType.valueOf(entry.key);
                    inventory.items.put(itemType, entry.value);
                } catch (IllegalArgumentException exception){
                    System.out.println(
                        "Ignoring unknown saved item: " + entry.key
                    );
                }
            }
        }
    }
}
