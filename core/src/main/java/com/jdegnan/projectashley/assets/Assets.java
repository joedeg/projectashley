package com.jdegnan.projectashley.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


/**
 * Manages the loading and retrieval of game assets using LibGDX's {@link AssetManager}.
 * This class provides a simplified interface for asset management in the project.
 */
public class Assets {

    private final AssetManager manager;

    public Assets() {
        manager = new AssetManager();

        manager.setLoader(TiledMap.class,
            new TmxMapLoader());
    }

    public <T> void acquire(AssetDescriptor<T> descriptor) {
        manager.load(descriptor);
    }

    public <T> boolean isLoaded(AssetDescriptor<T> descriptor) {
        return manager.isLoaded(descriptor.fileName);
    }

    public void loadGame() {

        // Global assets
        acquire(PlayerAssets.PLAYER_ATLAS);


        // Other global assets...
    }


    public void finishLoading() {
        manager.finishLoading();
    }

    /**
     * Updates the loading process. This should be called periodically (e.g., every frame)
     * if loading is done asynchronously.
     *
     * @return true if all assets are finished loading, false otherwise.
     */
    public boolean update() {
        return manager.update();
    }

    /**
     * Gets the progress of the current loading operation as a percentage.
     *
     * @return The loading progress between 0 and 1.
     */
    public float getProgress() {
        return manager.getProgress();
    }

    /**
     * Retrieves a loaded asset.
     *
     * @param <T>        The type of the asset.
     * @param descriptor The {@link AssetDescriptor} of the asset to retrieve.
     * @return The loaded asset.
     */
    public <T> T get(AssetDescriptor<T> descriptor) {
        return manager.get(descriptor);
    }

    public <T> void release(AssetDescriptor<T> descriptor) {
        manager.unload(descriptor.fileName);
    }

    /**
     * Disposes of the {@link AssetManager} and all its managed assets.
     */
    public void dispose() {
        manager.dispose();
    }


}
