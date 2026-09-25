package com.jdegnan.projectashley.level;


import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.config.GameConfig;

public class LevelLoader {

    private final Assets assets;
    private final LevelEntityLoader entityLoader;

    private LevelDefinition levelDefinition;

    private final GameConfig gameConfig;

    public LevelLoader(
        Assets assets,
        GameConfig gameConfig,
        LevelEntityLoader entityLoader
    ) {
        this.assets = assets;
        this.gameConfig = gameConfig;
        this.entityLoader = entityLoader;
    }

    /**
     * Begins loading the assets required by a level.
     *
     * <p>This method only requests the assets. Loading itself
     * happens asynchronously through update().</p>
     */
    public void requestLoad(LevelDefinition definition) {

        if (definition == null) {
            throw new IllegalArgumentException(
                "definition cannot be null."
            );
        }

        if (levelDefinition != null) {
            throw new IllegalStateException(
                "A level is already being loaded."
            );
        }

        this.levelDefinition = definition;

        assets.acquire(definition.getMap());

        for (AssetDescriptor<?> asset : definition.getAdditionalAssets()) {
            assets.acquire(asset);
        }
    }

    /**
     * Updates asynchronous asset loading.
     *
     * @return true when all assets required by the current
     * level have finished loading.
     */
    public boolean update() {
        if (levelDefinition == null) {
            return false;
        }

        assets.update();

        return isLoaded(levelDefinition);
    }

    /**
     * Checks whether all assets required by the current
     * level have finished loading.
     */
    public boolean isLoaded(LevelDefinition levelDefinition) {


        if (levelDefinition == null) {
            return false;
        }

        if (!assets.isLoaded(levelDefinition.getMap())) {
            return false;
        }

        for (AssetDescriptor<?> asset : levelDefinition.getAdditionalAssets()) {
            if (!assets.isLoaded(asset)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets the current loading progress.
     *
     * @return progress between 0 and 1.
     */
    public float getProgress() {
        if (levelDefinition == null) {
            return 1.0f;
        }
        return assets.getProgress();
    }


    /**
     * Builds the current level runtime.
     *
     * @return a fully constructed LevelRuntime.
     */
    public LevelRuntime build() {

        if (levelDefinition == null) {
            throw new IllegalStateException(
                "No level has been requested."
            );
        }

        if (!isLoaded(levelDefinition)) {
            throw new IllegalStateException(
                "Cannot build LevelRuntime before assets are loaded."
            );
        }

        TiledMap map = assets.get(levelDefinition.getMap());

        Level level = new Level(levelDefinition, map, gameConfig);

        LevelAssetSet assetSet = new LevelAssetSet(assets, levelDefinition);


        LevelRuntime runtime = new LevelRuntime(level, assetSet, entityLoader);

        this.levelDefinition = null;

        return runtime;
    }

    public void cancel() {
        if (levelDefinition == null) {
            return;
        }

        assets.release(levelDefinition.getMap());

        for (AssetDescriptor<?> asset : levelDefinition.getAdditionalAssets()) {
            assets.release(asset);
        }

        levelDefinition = null;
    }

    public boolean isLoading() {
        return levelDefinition != null;
    }
}
