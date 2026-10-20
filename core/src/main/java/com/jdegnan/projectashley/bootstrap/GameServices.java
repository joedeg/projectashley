package com.jdegnan.projectashley.bootstrap;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jdegnan.projectashley.assets.ItemAssetRegistry;
import com.jdegnan.projectashley.level.LevelManager;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.animations.AnimationLibrary;
import com.jdegnan.projectashley.factories.PlayerFactory;
import com.jdegnan.projectashley.rendering.RenderQueue;
import com.jdegnan.projectashley.save.CollectedPickupTracker;
import com.jdegnan.projectashley.save.SaveGameService;
import com.jdegnan.projectashley.save.SaveGameSnapshotFactory;

public class GameServices {

    private final Assets assets;

    private final PooledEngine engine;

    private final AnimationLibrary animationLibrary;

    private final PlayerFactory playerFactory;

    private final OrthographicCamera orthographicCamera;

    private final RenderQueue renderQueue;

    private final SpriteBatch spriteBatch;

    private final LevelManager levelManager;

    private final ItemAssetRegistry itemAssetRegistry;

    private final SaveGameService saveGameService;
    private final SaveGameSnapshotFactory saveGameSnapshotFactory;
    private final CollectedPickupTracker collectedPickupTracker;
    public GameServices(
        Assets assets,
        PooledEngine engine,
        AnimationLibrary library,
        PlayerFactory playerFactory,
        OrthographicCamera orthographicCamera,
        RenderQueue renderQueue,
        SpriteBatch spriteBatch,
        LevelManager levelManager,
        ItemAssetRegistry itemAssetRegistry,
        SaveGameService saveGameService,
        SaveGameSnapshotFactory saveGameSnapshotFactory,
        CollectedPickupTracker collectedPickupTracker) {

        this.assets = assets;
        this.engine = engine;
        this.animationLibrary = library;
        this.playerFactory = playerFactory;
        this.orthographicCamera = orthographicCamera;
        this.renderQueue = renderQueue;
        this.spriteBatch = spriteBatch;
        this.levelManager = levelManager;
        this.itemAssetRegistry = itemAssetRegistry;
        this.saveGameService = saveGameService;
        this.saveGameSnapshotFactory = saveGameSnapshotFactory;
        this.collectedPickupTracker = collectedPickupTracker;

    }

    public Assets getAssets() {
        return assets;
    }

    public PooledEngine getEngine() {
        return engine;
    }

    public AnimationLibrary getAnimationLibrary() {
        return animationLibrary;
    }

    public PlayerFactory getPlayerFactory() {
        return playerFactory;
    }

    public OrthographicCamera getOrthographicCamera(){
        return orthographicCamera;
    }

    public RenderQueue getRenderQueue(){
        return renderQueue;
    }

    public SpriteBatch getSpriteBatch(){
        return spriteBatch;
    }

    public LevelManager getLevelManager(){
        return levelManager;
    }

    public ItemAssetRegistry getItemAssetRegistry(){
        return itemAssetRegistry;
    }

    public SaveGameService getSaveGameService(){
        return saveGameService;
    }
    public SaveGameSnapshotFactory getSaveGameSnapshotFactory(){
        return saveGameSnapshotFactory;
    }

    public CollectedPickupTracker getCollectedPickupTracker() {
        return collectedPickupTracker;
    }
}
