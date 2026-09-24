package com.jdegnan.projectashley.bootstrap;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.jdegnan.projectashley.LevelLoader;
import com.jdegnan.projectashley.LevelManager;
import com.jdegnan.projectashley.SpatialGrid;

import com.jdegnan.projectashley.assets.Assets;


import com.jdegnan.projectashley.assets.animations.AnimationLibrary;
import com.jdegnan.projectashley.assets.animations.AnimationRegistry;
import com.jdegnan.projectashley.assets.animations.PlayerAnimationRegistry;

import com.jdegnan.projectashley.config.GameConfig;
import com.jdegnan.projectashley.factories.BulletFactory;
import com.jdegnan.projectashley.factories.PlayerFactory;

import com.jdegnan.projectashley.level.LevelEntityFactoryRegistry;
import com.jdegnan.projectashley.level.LevelEntityLoader;
import com.jdegnan.projectashley.level.TestEntityFactory;
import com.jdegnan.projectashley.rendering.RenderQueue;

import com.jdegnan.projectashley.systems.AnimationSystem;
import com.jdegnan.projectashley.systems.BulletHitSystem;
import com.jdegnan.projectashley.systems.CameraFollowSystem;
import com.jdegnan.projectashley.systems.ColliderSyncSystem;
import com.jdegnan.projectashley.systems.CollisionSystem;
import com.jdegnan.projectashley.systems.DamageSystem;
import com.jdegnan.projectashley.systems.DebugCollisionRenderSystem;
import com.jdegnan.projectashley.systems.LifetimeSystem;
import com.jdegnan.projectashley.systems.MovementCollisionSystem;
import com.jdegnan.projectashley.systems.PlayerAnimationStateSystem;
import com.jdegnan.projectashley.systems.PlayerInputSystem;
import com.jdegnan.projectashley.systems.RenderSubmissionSystem;

import com.jdegnan.projectashley.systems.SpatialPartitionSystem;
import com.jdegnan.projectashley.systems.WeaponSystem;

import java.util.List;


/**
 * Orchestrates the initialization of the game's core infrastructure.
 * <p>
 * This class follows the Bootstrap pattern, responsible for:
 * <ul>
 *   <li>Initializing LibGDX rendering components (SpriteBatch, Camera)</li>
 *   <li>Loading game assets</li>
 *   <li>Configuring the Ashley ECS Engine and its Systems</li>
 *   <li>Setting up game services and factories</li>
 *   <li>Initializing spatial partitioning for collisions</li>
 * </ul>
 */
public class GameBootStrap {

    /** The size of each cell in the spatial grid for collision detection. */
    private static final int CELL_SIZE = 64;

    /** Container for all core game services. */
    private final GameServices gameServices;




    /**
     * Initializes all game components and services.
     * Sets up the rendering batch, spatial grid, asset loading, animation library,
     * ECS engine, and registers all necessary systems.
     */
    public GameBootStrap() {

        GameConfig gameConfig = new GameConfig(32f);

        // -------------------------------------------------
        // 1. Core rendering objects
        // -------------------------------------------------

        SpriteBatch batch = new SpriteBatch();

        SpatialGrid grid = new SpatialGrid(CELL_SIZE);


        // -------------------------------------------------
        // 2. Core services
        // -------------------------------------------------

        Assets assets = createAssets();

        AnimationLibrary animationLibrary =
            createAnimationLibrary(assets);

        PooledEngine engine =
            createEngine();

        RenderQueue renderQueue =
            new RenderQueue();

        OrthographicCamera camera =
            createOrthographicCamera();

        CameraFollowSystem cameraFollowSystem = new CameraFollowSystem(camera);


        // -------------------------------------------------
        // 3. Factories
        // -------------------------------------------------

        BulletFactory bulletFactory =
            createBulletFactory(engine);

        PlayerFactory playerFactory =
            createPlayerFactory(animationLibrary);


        // -------------------------------------------------
        // 4. ECS systems
        // -------------------------------------------------

        registerSystems(
            engine,
            renderQueue,
            grid,
            camera,
            cameraFollowSystem,
            bulletFactory,
            batch
        );


        // -------------------------------------------------
        // 5. Level system
        // -------------------------------------------------

        LevelEntityFactoryRegistry factoryRegistry = new LevelEntityFactoryRegistry();
        TestEntityFactory testFactory = new TestEntityFactory(engine);
        factoryRegistry.register("test", testFactory);
        LevelEntityLoader levelEntityLoader = new LevelEntityLoader(factoryRegistry);


        LevelLoader levelLoader =
            new LevelLoader(assets, gameConfig, levelEntityLoader);

        LevelManager levelManager =
            new LevelManager(
                engine,
                levelLoader,
                playerFactory,
                cameraFollowSystem
            );


        // -------------------------------------------------
        // 6. Assemble GameServices
        // -------------------------------------------------

        gameServices =
            new GameServices(
                assets,
                engine,
                animationLibrary,
                playerFactory,
                camera,
                renderQueue,
                batch,
                levelManager
            );



    }


    /**
     * Loads and initializes the game assets.
     * @return The loaded assets.
     */
    private Assets createAssets() {

        Assets assets = new Assets();

        assets.loadGame();

        assets.finishLoading();

        return assets;
    }


    /**
     * Creates a new PooledEngine for the ECS.
     * @return A new PooledEngine instance.
     */
    private PooledEngine createEngine() {

        return new PooledEngine();
    }


    /**
     * Initializes the animation library and registers animation sets.
     * @param assets The assets to load animations from.
     * @return The populated animation library.
     */
    private AnimationLibrary createAnimationLibrary(
        Assets assets) {

        AnimationLibrary library =
            new AnimationLibrary();

        List<AnimationRegistry> registries =
            List.of(
                new PlayerAnimationRegistry()
            );

        for (AnimationRegistry registry : registries) {
            registry.register(
                assets,
                library
            );
        }

        return library;
    }


    /**
     * Creates a factory for bullet entities.
     * @param engine The ECS engine to associate with the factory.
     * @return A new BulletFactory instance.
     */
    private BulletFactory createBulletFactory(
        PooledEngine engine) {

        // TODO: Replace temporary atlas
        // with the real bullet atlas.

        TextureAtlas temporaryAtlas =
            new TextureAtlas();

        return new BulletFactory(
            engine,
            temporaryAtlas
        );
    }


    /**
     * Creates a factory for player entities.
     * @param library The animation library for player sprites.
     * @return A new PlayerFactory instance.
     */
    private PlayerFactory createPlayerFactory(
        AnimationLibrary library) {

        return new PlayerFactory(library);
    }


    /**
     * Creates and configures the orthographic camera.
     * @return A new OrthographicCamera instance.
     */
    private OrthographicCamera createOrthographicCamera() {

        OrthographicCamera camera = new OrthographicCamera();

        camera.zoom = 1.0f;

        return camera;
    }


    /**
     * Registers all ECS systems with the engine in the correct order.
     * @param engine The ECS engine.
     * @param renderQueue The queue for rendering submissions.
     * @param spatialGrid The grid for collision detection.
     * @param camera The camera for rendering and debug views.
     */
    private void registerSystems(
        PooledEngine engine,
        RenderQueue renderQueue,
        SpatialGrid spatialGrid,
        OrthographicCamera camera,
        CameraFollowSystem cameraFollowSystem,
        BulletFactory bulletFactory,
        SpriteBatch batch) {

        engine.addSystem(
            new MovementCollisionSystem()
        );

        engine.addSystem(
            new ColliderSyncSystem()
        );

        engine.addSystem(
            new CollisionSystem()
        );

        engine.addSystem(
            cameraFollowSystem
        );


        engine.addSystem(
            new AnimationSystem()
        );

        engine.addSystem(
            new BulletHitSystem()
        );



        engine.addSystem(
            new DamageSystem()
        );

        engine.addSystem(
            new DebugCollisionRenderSystem(camera)
        );

        engine.addSystem(
            new LifetimeSystem()
        );

        engine.addSystem(
            new PlayerAnimationStateSystem()
        );

        engine.addSystem(
            new PlayerInputSystem()
        );

        engine.addSystem(
            new SpatialPartitionSystem(spatialGrid)
        );

        engine.addSystem(
            new WeaponSystem(bulletFactory)
        );

        engine.addSystem(
            new RenderSubmissionSystem(renderQueue)
        );
    }


    /**
     * Returns the container for all initialized game services.
     * @return The game services instance.
     */
    public GameServices getGameServices() {

        return gameServices;
    }
}
