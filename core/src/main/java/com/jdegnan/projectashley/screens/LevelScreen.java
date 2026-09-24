package com.jdegnan.projectashley.screens;


import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jdegnan.projectashley.LevelManager;
import com.jdegnan.projectashley.LevelRuntime;
import com.jdegnan.projectashley.Levels;
import com.jdegnan.projectashley.bootstrap.GameServices;
import com.jdegnan.projectashley.rendering.RenderContext;
import com.jdegnan.projectashley.rendering.RenderPipeline;


public class LevelScreen extends BaseScreen {
    private final PooledEngine engine;
    private final RenderPipeline renderPipeline;
    protected final RenderContext renderContext;
    private final LevelManager levelManager;

    public LevelScreen(GameServices services) {
        super(services.getOrthographicCamera());
        this.engine = services.getEngine();
        this.levelManager = services.getLevelManager();

        renderContext = new RenderContext(
            services.getOrthographicCamera(),
            fitViewport,
            services.getRenderQueue(),
            services.getSpriteBatch(),
            new ShapeRenderer()
        );

        levelManager.loadInitialLevel(Levels.ADVENTURE);

        renderPipeline = new RenderPipeline(renderContext.getSpriteBatch());
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
    }
}
