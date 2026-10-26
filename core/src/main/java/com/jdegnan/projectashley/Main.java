package com.jdegnan.projectashley;

import com.badlogic.gdx.Game;
import com.jdegnan.projectashley.bootstrap.GameBootStrap;
import com.jdegnan.projectashley.screens.LoadingScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {


    private GameBootStrap gameBootStrap;

    @Override
    public void create() {

        gameBootStrap = new GameBootStrap();

        setScreen(new LoadingScreen(this, gameBootStrap));
    }

    @Override
    public void render() {
        super.render();
    }
}
