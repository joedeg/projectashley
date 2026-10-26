package com.jdegnan.projectashley.screens;


import com.badlogic.gdx.Game;
import com.jdegnan.projectashley.bootstrap.GameBootStrap;


public class LoadingScreen extends BaseScreen{

    private final GameBootStrap gameBootStrap;
    private final Game game;

    public LoadingScreen(Game game, GameBootStrap gameBootStrap){
        this.game = game;
        this.gameBootStrap = gameBootStrap;
    }

    @Override
    public void update(float deltaTime) {
        boolean finished = gameBootStrap.getGameServices().getAssets().update();

        float progressAmount = gameBootStrap.getGameServices().getAssets().getProgress();


        System.out.println("loading assets " + progressAmount * 100 + "%");

        if(finished) {
            game.setScreen(new LevelScreen(gameBootStrap.getGameServices()));
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if(gameBootStrap != null){
            gameBootStrap.dispose();
        }
    }
}
