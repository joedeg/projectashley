package com.jdegnan.projectashley.rendering;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapRenderer {
    private final
    OrthogonalTiledMapRenderer renderer;

    public MapRenderer(
        TiledMap map)
    {
        renderer =
            new OrthogonalTiledMapRenderer(
                map);
    }

    public void render(
        RenderContext context)
    {
        renderer.setView(
            context.getCamera());

        renderer.render();
    }

    public void renderGround(
        RenderContext context)
    {
        renderer.setView(
            context.getCamera());

        renderer.render(
            new int[] {0,1});
    }

    public void renderAbovePlayer(
        RenderContext context)
    {
        renderer.setView(
            context.getCamera());

        renderer.render(
            new int[] {2});
    }

    public void dispose() {
        renderer.dispose();
    }
}
