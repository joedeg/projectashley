package com.jdegnan.projectashley.rendering;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class RenderQueue {
    private final Array<RenderCommand> commands = new Array<>();

    private final Pool<RenderCommand> pool = new Pool<RenderCommand>() {
        @Override
        protected RenderCommand newObject() {
            return new RenderCommand();
        }
    };

    public RenderCommand obtain(){
        return pool.obtain();
    }

    public void submit(RenderCommand command){

        commands.add(command);
    }

    public Array<RenderCommand> getCommands(){
        return commands;
    }

    public void clear(){

        for(RenderCommand command : commands){
            pool.free(command);
        }
        commands.clear();
    }
}
