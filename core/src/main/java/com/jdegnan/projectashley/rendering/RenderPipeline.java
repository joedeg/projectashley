package com.jdegnan.projectashley.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

public class RenderPipeline {
    private final SpriteBatch batch;

    private final Sort sort = new Sort();

    public RenderPipeline(SpriteBatch batch){
        this.batch = batch;
    }

    public void render(RenderContext context){
        RenderQueue queue = context.getRenderQueue();
        Array<RenderCommand> commands = queue.getCommands();

        sort.sort(commands, (a,b) -> {
            if(a.layer != b.layer){
                return Integer.compare(a.layer, b.layer);
            }
            return Float.compare(b.sortY, a.sortY);
        });

        batch.setProjectionMatrix(context.getCamera().combined);
        batch.begin();

        for(RenderCommand command : commands){
            batch.setColor(command.color);

            batch.draw(
                command.region,

                command.x,
                command.y,

                command.originX,
                command.originY,

                command.width,
                command.height,

                command.scaleX,
                command.scaleY,

                command.rotation
            );
        }

        batch.setColor(1,1,1,1);

        batch.end();

        queue.clear();
    }
}
