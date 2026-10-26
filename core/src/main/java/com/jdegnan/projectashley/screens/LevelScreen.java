package com.jdegnan.projectashley.screens;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.jdegnan.projectashley.assets.AudioAssets;
import com.jdegnan.projectashley.assets.ItemAssets;
import com.jdegnan.projectashley.audio.AudioManager;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.ItemPickupComponent;
import com.jdegnan.projectashley.factories.ItemPickupFactory;
import com.jdegnan.projectashley.factories.ParticleFactory;
import com.jdegnan.projectashley.inventory.InventoryUIRenderer;
import com.jdegnan.projectashley.inventory.ItemType;
import com.jdegnan.projectashley.level.LevelDefinition;
import com.jdegnan.projectashley.level.LevelManager;
import com.jdegnan.projectashley.level.LevelRuntime;
import com.jdegnan.projectashley.level.Levels;
import com.jdegnan.projectashley.bootstrap.GameServices;
import com.jdegnan.projectashley.rendering.GateGlowRenderer;
import com.jdegnan.projectashley.rendering.InteractionMessageRenderer;
import com.jdegnan.projectashley.rendering.ParticleRenderer;
import com.jdegnan.projectashley.rendering.RenderContext;
import com.jdegnan.projectashley.rendering.RenderPipeline;
import com.jdegnan.projectashley.save.CollectedPickupTracker;
import com.jdegnan.projectashley.save.DefeatedEnemyTracker;
import com.jdegnan.projectashley.save.SaveGameData;
import com.jdegnan.projectashley.save.SaveGameSerializer;
import com.jdegnan.projectashley.save.SaveGameService;
import com.jdegnan.projectashley.save.SaveGameSnapshotFactory;
import com.jdegnan.projectashley.save.SaveGameRestorer;
import com.jdegnan.projectashley.systems.AreaTransitionSystem;
import com.jdegnan.projectashley.ui.HealthHud;

import org.apache.tools.ant.taskdefs.optional.depend.constantpool.Utf8CPInfo;


public class LevelScreen extends BaseScreen {
    private final PooledEngine engine;
    private final RenderPipeline renderPipeline;
    protected final RenderContext renderContext;
    private final LevelManager levelManager;

    private final GateGlowRenderer gateGlowRenderer;

    private final ParticleRenderer particleRenderer;

    private final InteractionMessageRenderer interactionMessageRenderer;

    private final InventoryUIRenderer inventoryUIRenderer;

    private final ParticleFactory particleFactory;

    private final HealthHud healthHud;

    private final SaveGameService saveGameService;
    private final SaveGameSnapshotFactory saveGameSnapshotFactory;
    private final SaveGameRestorer saveGameRestorer;
    private final CollectedPickupTracker collectedPickupTracker;
    private final DefeatedEnemyTracker defeatedEnemyTracker;
    private SaveGameData pendingSaveRestore;
    private final ItemPickupFactory itemPickupFactory;

    private final TransitionController transitionController;
    private AreaTransitionController areaTransitionController;

    private final AudioManager audioManager;

    public LevelScreen(GameServices gameServices) {
        super(gameServices.getOrthographicCamera());

        this.engine = gameServices.getEngine();
        this.levelManager = gameServices.getLevelManager();
        this.saveGameService = gameServices.getSaveGameService();
        this.saveGameSnapshotFactory =
            gameServices.getSaveGameSnapshotFactory();
        this.saveGameRestorer = new SaveGameRestorer();
        this.itemPickupFactory = gameServices.getItemPickupFactory();
        this.audioManager = gameServices.getAudioManager();


        renderContext = new RenderContext(
            gameServices.getOrthographicCamera(),
            fitViewport,
            gameServices.getRenderQueue(),
            gameServices.getSpriteBatch(),
            new ShapeRenderer()
        );

        transitionController = new TransitionController(
            gameServices.getOrthographicCamera()
        );
        areaTransitionController = new AreaTransitionController(
            transitionController,
            levelManager
        );

        if(engine.getSystem(AreaTransitionSystem.class) == null) {
            engine.addSystem(new AreaTransitionSystem(areaTransitionController));
        }

        levelManager.loadInitialLevel(Levels.ADVENTURE);
        audioManager.playMusic(
            gameServices.getAssets(),
            AudioAssets.OVERWORLD_MUSIC,
            true
        );

        renderPipeline = new RenderPipeline(renderContext.getSpriteBatch());

        inventoryUIRenderer = new InventoryUIRenderer(
            gameServices.getSpriteBatch(),
            gameServices.getAssets(),
            gameServices.getItemAssetRegistry()
        );

        interactionMessageRenderer = new InteractionMessageRenderer(
            gameServices.getSpriteBatch()
        );

        gateGlowRenderer =
            new GateGlowRenderer(
                gameServices.getOrthographicCamera()
            );
        gateGlowRenderer.addedToEngine(engine);

        particleRenderer = new ParticleRenderer(
            gameServices.getOrthographicCamera()
        );
        particleRenderer.addedToEngine(engine);

        particleFactory = new ParticleFactory(engine);


        healthHud = new HealthHud(gameServices.getAssets().get(ItemAssets.HEART_HUD));

        this.collectedPickupTracker = gameServices.getCollectedPickupTracker();
        this.defeatedEnemyTracker = gameServices.getDefeatedEnemyTracker();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        inventoryUIRenderer.resize(width, height);
        interactionMessageRenderer.resize(width, height);
    }

    @Override
    public void update(float deltaTime) {
        levelManager.update();
        areaTransitionController.update();

        if (pendingSaveRestore != null
            && !levelManager.isTransitioning()) {
            saveGameRestorer.restorePlayer(
                pendingSaveRestore,
                levelManager);


            saveGameRestorer.restoreOpenedDoor(
                pendingSaveRestore,
                levelManager);

            for (SaveGameData.DroppedItemData droppedItem
                : pendingSaveRestore.droppedItems) {

                if (droppedItem.id == null
                    || droppedItem.itemType == null) {
                    continue;
                }

                 itemPickupFactory.createDrop(
                    ItemType.valueOf(droppedItem.itemType),
                    droppedItem.x,
                    droppedItem.y,
                    droppedItem.id
                );

            }

            pendingSaveRestore = null;

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {

            SaveGameData snapshot =
                saveGameSnapshotFactory.create(levelManager);

            saveGameService.save(snapshot);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {

            SaveGameData loaded = saveGameService.load();


            if (loaded != null) {
                collectedPickupTracker.restore(loaded.collectedPickupIds);
                defeatedEnemyTracker.restore(loaded.defeatedEnemyIds);

                pendingSaveRestore = loaded;

                LevelDefinition savedLevel = Levels.getById(loaded.levelId);
                if(savedLevel == null){
                    throw new IllegalStateException(
                        "No level with id: " + loaded.levelId
                    );
                }
                levelManager.requestTransition(
                    savedLevel,
                    loaded.playerX,
                    loaded.playerY
                );

            } else {
                System.out.println("NO SAVE FILE FOUND");
            }
        }

        LevelRuntime runtime = levelManager.getCurrentRuntime();
        if (runtime != null) {
            runtime.renderMap(renderContext);
        }

        engine.update(deltaTime);
        renderPipeline.render(renderContext);

        transitionController.update(deltaTime);

        SpriteBatch batch = renderContext.getSpriteBatch();
        Matrix4 worldProjection = batch.getProjectionMatrix().cpy();
        batch.setProjectionMatrix(
            new

                Matrix4().

                setToOrtho2D(
                    0,
                    0,
                    fitViewport.getWorldWidth(),
                    fitViewport.getWorldHeight()));

        batch.begin();

        Entity player = levelManager.getPlayer();

        if (player != null) {
            HealthComponent health =
                levelManager.getPlayer().getComponent(HealthComponent.class);

            if (health != null) {
                healthHud.render(batch, health.hp, health.maxHp);
            }
        }

        batch.end();
        batch.setProjectionMatrix(worldProjection);

        gateGlowRenderer.render();

        particleRenderer.render();

        inventoryUIRenderer.render(levelManager.getPlayer());

        interactionMessageRenderer.render(levelManager.getPlayer());

        transitionController.render();
    }

    @Override
    public void dispose() {

        AreaTransitionSystem system =
            engine.getSystem(AreaTransitionSystem.class);

        if (system != null) {
            engine.removeSystem(system);
        }

        inventoryUIRenderer.dispose();
        transitionController.dispose();
        super.dispose();

    }
}
