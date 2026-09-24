package com.jdegnan.projectashley;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.factories.PlayerFactory;
import com.jdegnan.projectashley.systems.CameraFollowSystem;

public class LevelManager {

    private final PooledEngine engine;
    private final LevelLoader levelLoader;
    private LevelRuntime currentRuntime;
    private boolean transitionRequested;

    private final PlayerFactory playerFactory;

    private final CameraFollowSystem cameraFollowSystem;

    private Entity player;

    public LevelManager(PooledEngine engine,
                        LevelLoader levelLoader,
                        PlayerFactory playerFactory,
                        CameraFollowSystem cameraFollowSystem) {
        this.engine = engine;
        this.levelLoader = levelLoader;
        this.playerFactory = playerFactory;
        this.cameraFollowSystem = cameraFollowSystem;
    }

    public void loadInitialLevel(LevelDefinition definition) {
        if(currentRuntime != null){
            throw new IllegalStateException(
                "A level is already active."
            );
        }

        requestTransition(definition);
    }

    public void requestTransition(LevelDefinition definition) {
        if(definition == null){
            throw new IllegalArgumentException(
                "Level definition cannot be null."
            );
        }

        if(transitionRequested){
            throw new  IllegalStateException(
                "A level transition is already requested."
            );
        }

        levelLoader.requestLoad(definition);

        transitionRequested = true;
    }

    public void update() {
        if (!transitionRequested) {
            return;
        }


        if (!levelLoader.update()) {
            return;
        }

        LevelRuntime newRuntime = levelLoader.build();

        switchTo(newRuntime);

        transitionRequested = false;
    }

    private void switchTo(LevelRuntime newRuntime) {
        LevelRuntime oldRuntime = currentRuntime;

        newRuntime.addTo(engine);
        currentRuntime = newRuntime;

        cameraFollowSystem.setMapBounds(newRuntime.getMapBounds());

        spawnPlayer(newRuntime);

        if (oldRuntime != null) {
            oldRuntime.removeFrom(engine);
            oldRuntime.dispose();
        }
    }

    private void spawnPlayer(LevelRuntime runtime) {
        Vector2 spawn = runtime.getDefinition().getPlayerSpawn();

        if (player == null) {
            player = playerFactory.create(spawn.x, spawn.y);
            engine.addEntity(player);
        } else {
            setPlayerPosition(player, spawn.x, spawn.y);
        }
    }

    private void setPlayerPosition(Entity player, float x, float y){
        PositionComponent position = player.getComponent(PositionComponent.class);
        ColliderComponent collider = player.getComponent(ColliderComponent.class);

        position.x = x;
        position.y = y;

        collider.localBounds.setPosition(x,y);
    }

    public void cancelTransition(){
        if(!transitionRequested){
            return;
        }
        levelLoader.cancel();
        transitionRequested = false;
    }

    public LevelRuntime getCurrentRuntime() {
        return currentRuntime;
    }

    public Entity getPlayer(){
        return player;
    }

    public boolean isTransitioning(){
        return transitionRequested;
    }

    public float getLoadingProgress(){

        if(!transitionRequested){
            return 1.0f;
        }
        return levelLoader.getProgress();
    }
}

