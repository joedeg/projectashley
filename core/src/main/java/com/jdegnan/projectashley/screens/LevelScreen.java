package com.jdegnan.projectashley.screens;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.jdegnan.projectashley.assets.ItemAssets;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.factories.ParticleFactory;
import com.jdegnan.projectashley.inventory.InventoryUIRenderer;
import com.jdegnan.projectashley.level.LevelManager;
import com.jdegnan.projectashley.level.LevelRuntime;
import com.jdegnan.projectashley.level.Levels;
import com.jdegnan.projectashley.bootstrap.GameServices;
import com.jdegnan.projectashley.rendering.GateGlowRenderer;
import com.jdegnan.projectashley.rendering.InteractionMessageRenderer;
import com.jdegnan.projectashley.rendering.ParticleRenderer;
import com.jdegnan.projectashley.rendering.RenderContext;
import com.jdegnan.projectashley.rendering.RenderPipeline;
import com.jdegnan.projectashley.ui.HealthHud;


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

    private HealthHud healthHud;

    public LevelScreen(GameServices gameServices) {
        super(gameServices.getOrthographicCamera());
        this.engine = gameServices.getEngine();
        this.levelManager = gameServices.getLevelManager();

        renderContext = new RenderContext(
            gameServices.getOrthographicCamera(),
            fitViewport,
            gameServices.getRenderQueue(),
            gameServices.getSpriteBatch(),
            new ShapeRenderer()
        );

        levelManager.loadInitialLevel(Levels.ADVENTURE);

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

        LevelRuntime runtime = levelManager.getCurrentRuntime();
        if (runtime != null) {
            runtime.renderMap(renderContext);
        }

        engine.update(deltaTime);
        renderPipeline.render(renderContext);

        SpriteBatch batch = renderContext.getSpriteBatch();
        Matrix4 worldProjection = batch.getProjectionMatrix().cpy();
        batch.setProjectionMatrix(
            new Matrix4().setToOrtho2D(
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
    }

    @Override
    public void dispose() {
        inventoryUIRenderer.dispose();
        super.dispose();

    }
}
