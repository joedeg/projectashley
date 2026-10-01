package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.jdegnan.projectashley.components.InventoryComponent;
import com.jdegnan.projectashley.components.InventoryUIComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.inventory.ItemType;


public class InventoryUIInputSystem extends EntitySystem {

    private ImmutableArray<Entity> players;

    public InventoryUIInputSystem(){
    }
    @Override
    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(
            Family.all(
                PlayerComponent.class,
                InventoryComponent.class,
                InventoryUIComponent.class
            ).get());
    }

    @Override
    public void update(float deltaTime) {
        if(players.size() == 0) return;

        Entity player = players.first();

        InventoryComponent inventory =
            player.getComponent(InventoryComponent.class);

        InventoryUIComponent ui =
            player.getComponent(InventoryUIComponent.class);

        if(!ui.visible) return;

        Array<ItemType> ownedItems = new Array<>();

        for(ItemType item : ItemType.values()){
            if(inventory.getCount(item) > 0){
                ownedItems.add(item);
            }
        }

        if(ownedItems.size == 0){
            ui.selectedItem = null;
            return;
        }

        int selectedIndex = ownedItems.indexOf(ui.selectedItem, true);

        if(selectedIndex < 0){
            ui.selectedItem = ownedItems.first();
            selectedIndex = 0;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            selectedIndex = (selectedIndex + 1) % ownedItems.size;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            selectedIndex =
                (selectedIndex - 1 + ownedItems.size) % ownedItems.size;
        }

        ui.selectedItem = ownedItems.get(selectedIndex);

        System.out.println(ui.selectedItem);

    }
}
