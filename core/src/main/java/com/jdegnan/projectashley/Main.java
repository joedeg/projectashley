package com.jdegnan.projectashley;

import com.badlogic.gdx.Game;
import com.jdegnan.projectashley.screens.LoadingScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {



    @Override
    public void create() {


        setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
