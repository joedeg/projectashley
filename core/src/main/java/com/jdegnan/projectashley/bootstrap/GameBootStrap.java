package com.jdegnan.projectashley.bootstrap;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.jdegnan.projectashley.SpatialGrid;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.ItemAssetRegistry;
import com.jdegnan.projectashley.assets.ItemAssets;
import com.jdegnan.projectashley.assets.animations.AnimationLibrary;
import com.jdegnan.projectashley.assets.animations.AnimationRegistry;
import com.jdegnan.projectashley.assets.animations.PlayerAnimationRegistry;
import com.jdegnan.projectashley.config.GameConfig;
import com.jdegnan.projectashley.factories.BulletFactory;
import com.jdegnan.projectashley.factories.ChestFactory;
import com.jdegnan.projectashley.factories.DoorFactory;
import com.jdegnan.projectashley.factories.ItemPickupFactory;
import com.jdegnan.projectashley.factories.PlayerFactory;
import com.jdegnan.projectashley.inventory.ItemDefinition;
import com.jdegnan.projectashley.inventory.ItemType;
import com.jdegnan.projectashley.level.ChestRewardSystem;
import com.jdegnan.projectashley.level.LevelEntityFactoryRegistry;
import com.jdegnan.projectashley.level.LevelEntityLoader;
import com.jdegnan.projectashley.level.LevelLoader;
import com.jdegnan.projectashley.level.LevelManager;
import com.jdegnan.projectashley.rendering.RenderQueue;
import com.jdegnan.projectashley.systems.AnimationSystem;
import com.jdegnan.projectashley.systems.BulletHitSystem;
import com.jdegnan.projectashley.systems.CameraFollowSystem;
import com.jdegnan.projectashley.systems.ChestAnimationSystem;
import com.jdegnan.projectashley.systems.ChestInteractionSystem;
import com.jdegnan.projectashley.systems.ColliderSyncSystem;
import com.jdegnan.projectashley.systems.CollisionSystem;
import com.jdegnan.projectashley.systems.DamageSystem;
import com.jdegnan.projectashley.systems.DebugCollisionRenderSystem;
import com.jdegnan.projectashley.systems.DoorInteractionSystem;
import com.jdegnan.projectashley.systems.InteractionMessageSystem;
import com.jdegnan.projectashley.systems.InventoryUIInputSystem;
import com.jdegnan.projectashley.systems.InventoryUISystem;
import com.jdegnan.projectashley.systems.ItemPickupSystem;
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
 * This class follows the Bootstrap pattern and is responsible for:
 * <ul>
 *   <li>Initializing LibGDX rendering components ({@link SpriteBatch}, {@link OrthographicCamera}, {@link RenderQueue})</li>
 *   <li>Loading game assets and configuring animation libraries</li>
 *   <li>Initializing spatial partitioning for collision detection</li>
 *   <li>Configuring the Ashley ECS engine and registering systems in execution order</li>
 *   <li>Instantiating entity factories and item registries</li>
 *   <li>Setting up level loading and level management infrastructure</li>
 *   <li>Assembling and exposing the unified {@link GameServices} container</li>
 * </ul>
 */
public class GameBootStrap {

    /**
     * Default unit size in pixels for spatial grid cells used in collision detection.
     */
    private static final int CELL_SIZE = 64;

    /**
     * World tile size in units used for game configuration.
     */
    private static final float TILE_SIZE = 32f;

    /**
     * Container holding all core game services initialized during bootstrapping.
     */
    private final GameServices gameServices;

    /**
     * Initializes all game infrastructure, services, factories, systems, and level managers.
     */
    public GameBootStrap() {
        GameConfig gameConfig = new GameConfig(TILE_SIZE);

        // 1. Rendering infrastructure
        SpriteBatch batch = new SpriteBatch();
        OrthographicCamera camera = createOrthographicCamera();
        RenderQueue renderQueue = new RenderQueue();

        // 2. Core engine & asset management
        Assets assets = createAssets();
        SpatialGrid grid = new SpatialGrid(CELL_SIZE);
        PooledEngine engine = createEngine();
        AnimationLibrary animationLibrary = createAnimationLibrary(assets);

        // 3. Item registry & entity factories
        ItemAssetRegistry itemAssetRegistry = createItemAssetRegistry();
        BulletFactory bulletFactory = createBulletFactory(engine);
        PlayerFactory playerFactory = createPlayerFactory(animationLibrary);

        // 4. Systems
        CameraFollowSystem cameraFollowSystem = new CameraFollowSystem(camera);
        registerSystems(engine, renderQueue, grid, camera, cameraFollowSystem, bulletFactory, itemAssetRegistry);

        // 5. Level management
        LevelManager levelManager = createLevelManager(
            engine,
            assets,
            gameConfig,
            playerFactory,
            cameraFollowSystem,
            itemAssetRegistry
        );

        // 6. Service container assembly
        this.gameServices = new GameServices(
            assets,
            engine,
            animationLibrary,
            playerFactory,
            camera,
            renderQueue,
            batch,
            levelManager,
            itemAssetRegistry
        );
    }

    /**
     * Creates and begins loading game assets asynchronously.
     *
     * @return The initialized {@link Assets} instance with asset loading queued.
     */
    private Assets createAssets() {
        Assets assets = new Assets();
        assets.loadGame();
        assets.finishLoading();
        return assets;
    }

    /**
     * Instantiates a new pooled Ashley ECS engine.
     *
     * @return A new {@link PooledEngine} instance.
     */
    private PooledEngine createEngine() {
        return new PooledEngine();
    }

    /**
     * Initializes the animation library and registers all animation sets from registries.
     *
     * @param assets The asset manager holding loaded animation textures and atlases.
     * @return The populated {@link AnimationLibrary}.
     */
    private AnimationLibrary createAnimationLibrary(Assets assets) {
        AnimationLibrary library = new AnimationLibrary();
        List<AnimationRegistry> registries = List.of(
            new PlayerAnimationRegistry()
        );

        for (AnimationRegistry registry : registries) {
            registry.register(assets, library);
        }

        return library;
    }

    /**
     * Initializes the item asset registry and populates default item definitions.
     *
     * @return The configured {@link ItemAssetRegistry}.
     */
    private ItemAssetRegistry createItemAssetRegistry() {
        ItemAssetRegistry itemAssetRegistry = new ItemAssetRegistry();

        itemAssetRegistry.register(
            new ItemDefinition(
                ItemType.FOREST_KEY,
                "Forest Key",
                "A key that opens the gate of the forest.",
                ItemAssets.FOREST_KEY
            )
        );

        itemAssetRegistry.register(
            new ItemDefinition(
                ItemType.MARSH_AMULET,
                "Marsh Amulet",
                "An ancient amulet said to hold the power of the marsh.",
                ItemAssets.MARSH_AMULET
            )
        );

        return itemAssetRegistry;
    }

    /**
     * Creates a factory for bullet entities.
     *
     * @param engine The Ashley ECS engine where bullet entities will be added.
     * @return A new {@link BulletFactory} instance.
     */
    private BulletFactory createBulletFactory(PooledEngine engine) {
        // TODO: Replace temporary atlas with the real bullet atlas.
        TextureAtlas temporaryAtlas = new TextureAtlas();
        return new BulletFactory(engine, temporaryAtlas);
    }

    /**
     * Creates a factory for player entities.
     *
     * @param library The animation library providing player sprite animations.
     * @return A new {@link PlayerFactory} instance.
     */
    private PlayerFactory createPlayerFactory(AnimationLibrary library) {
        return new PlayerFactory(library);
    }

    /**
     * Creates and configures the orthographic game camera.
     *
     * @return A configured {@link OrthographicCamera} instance.
     */
    private OrthographicCamera createOrthographicCamera() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.zoom = 1.0f;
        return camera;
    }

    /**
     * Configures entity factory registries and constructs the level management pipeline.
     *
     * @param engine             The Ashley ECS engine.
     * @param assets             The game assets manager.
     * @param gameConfig         The game configuration settings.
     * @param playerFactory      Factory for spawning the player entity.
     * @param cameraFollowSystem Camera tracking system used when spawning/loading levels.
     * @param itemAssetRegistry  Registry containing item definitions for pickup entities.
     * @return The initialized {@link LevelManager}.
     */
    private LevelManager createLevelManager(
        PooledEngine engine,
        Assets assets,
        GameConfig gameConfig,
        PlayerFactory playerFactory,
        CameraFollowSystem cameraFollowSystem,
        ItemAssetRegistry itemAssetRegistry
    ) {

        LevelEntityFactoryRegistry factoryRegistry = new LevelEntityFactoryRegistry();
        LevelEntityLoader levelEntityLoader = new LevelEntityLoader(factoryRegistry);

        factoryRegistry.register("chest", new ChestFactory(engine, assets));
        factoryRegistry.register("door", new DoorFactory(engine, assets));
        factoryRegistry.register("item", new ItemPickupFactory(engine, assets, itemAssetRegistry));

        LevelLoader levelLoader = new LevelLoader(assets, gameConfig, levelEntityLoader);

        return new LevelManager(
            engine,
            levelLoader,
            playerFactory,
            cameraFollowSystem
        );
    }

    /**
     * Registers all ECS systems with the Ashley engine in their required processing order.
     *
     * @param engine             The Ashley ECS engine.
     * @param renderQueue        Queue for submitting renderable components.
     * @param spatialGrid        Grid used for spatial partition collision queries.
     * @param camera             Camera used for rendering and debug visualizations.
     * @param cameraFollowSystem System updating camera positioning based on target entities.
     * @param bulletFactory      Factory for spawning projectile entities upon shooting.
     * @param itemAssetRegistry  Registry containing item definitions.
     */
    private void registerSystems(
        PooledEngine engine,
        RenderQueue renderQueue,
        SpatialGrid spatialGrid,
        OrthographicCamera camera,
        CameraFollowSystem cameraFollowSystem,
        BulletFactory bulletFactory,
        ItemAssetRegistry itemAssetRegistry
    ) {
        // Physics & Movement Systems
        engine.addSystem(new MovementCollisionSystem());
        engine.addSystem(new ColliderSyncSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new SpatialPartitionSystem(spatialGrid));

        // Camera & World Systems
        engine.addSystem(cameraFollowSystem);

        // Gameplay, Logic & Interaction Systems
        engine.addSystem(new PlayerInputSystem());
        engine.addSystem(new WeaponSystem(bulletFactory));
        engine.addSystem(new BulletHitSystem());
        engine.addSystem(new DamageSystem());
        engine.addSystem(new LifetimeSystem());

        // Environment & Interactive Object Systems
        engine.addSystem(new ChestInteractionSystem());
        engine.addSystem(new ChestAnimationSystem());
        engine.addSystem(new ChestRewardSystem());
        engine.addSystem(new DoorInteractionSystem(itemAssetRegistry));
        engine.addSystem(new ItemPickupSystem());
        engine.addSystem(new InteractionMessageSystem());

        // Inventory & UI Systems
        engine.addSystem(new InventoryUISystem());
        engine.addSystem(new InventoryUIInputSystem());

        // Visual & Animation State Systems
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new PlayerAnimationStateSystem());

        // Rendering & Debug Systems
        engine.addSystem(new DebugCollisionRenderSystem(camera));
        engine.addSystem(new RenderSubmissionSystem(renderQueue));
    }

    /**
     * Returns the container holding all initialized game services and infrastructure references.
     *
     * @return The initialized {@link GameServices} container.
     */
    public GameServices getGameServices() {
        return gameServices;
    }
}
