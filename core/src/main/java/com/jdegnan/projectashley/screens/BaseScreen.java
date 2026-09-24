package com.jdegnan.projectashley.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class BaseScreen implements Screen, InputProcessor {

    public static final float VIEWPORT_WIDTH = 800;
    public static final float VIEWPORT_HEIGHT = 400;
    protected final Stage mainStage;

    protected final FitViewport fitViewport;

    public BaseScreen() {
        this(new OrthographicCamera());
    }

    public BaseScreen(OrthographicCamera camera) {
        fitViewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);

        mainStage = new Stage(fitViewport);
    }

    public abstract void update(float deltaTime);

    @Override
    public void show() {
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(this);
        im.addProcessor(mainStage);
        Gdx.input.setInputProcessor(im);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainStage.act(delta);

        mainStage.getViewport().apply();
        update(delta);

        mainStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        im.removeProcessor(this);
        im.removeProcessor(mainStage);

        mainStage.dispose();
    }

    @Override
    public void dispose() {
        mainStage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
