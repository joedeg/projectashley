package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.TagComponents.DeadComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.level.LevelManager;

public class PlayerDeathSystem extends EntitySystem {
    private ImmutableArray<Entity> players;

    private boolean deathHandled;

    private float respawnTimer = 0;
    private static final float RESPAWN_DELAY = 2f;

    private final LevelManager levelManager;

    public PlayerDeathSystem(LevelManager levelManager) {
        this.levelManager = levelManager;
    }


    @Override
    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(
            Family.all(
                PlayerComponent.class
            ).get());
    }

    @Override
    public void update(float deltaTime) {
        for(Entity player : players){
            HealthComponent health =
                player.getComponent(HealthComponent.class);

            if(health.hp <= 0 && !deathHandled){
                deathHandled = true;
                respawnTimer = RESPAWN_DELAY;
                player.add(new DeadComponent());

            }

            if(deathHandled){

                respawnTimer -= deltaTime;

                if(respawnTimer <= 0){
                    levelManager.respawnPlayer();
                    player.remove(DeadComponent.class);
                    deathHandled = false;
                    respawnTimer = 0f;

                }
            }
        }
    }
}
