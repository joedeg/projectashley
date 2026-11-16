package com.jdegnan.projectashley.rendering;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

public class MapRenderer {
    private final
    OrthogonalTiledMapRenderer renderer;
    private final int[] groundLayers;
    private final int[] foregroundLayers;


    public MapRenderer(
        TiledMap map) {
        renderer =
            new OrthogonalTiledMapRenderer(
                map);

        groundLayers = layerIndices(
            map,
            "Ground",
            "Behind",
            "Walls",
            "Decoration"
        );

        foregroundLayers = layerIndices(
            map,
            "Front",
            "Above"
        );


    }

    private int[] layerIndices(TiledMap map, String... names) {
        Array<Integer> indices = new Array<>();

        for (String name : names) {
            MapLayer layer = map.getLayers().get(name);
            if (layer != null) {
                indices.add(map.getLayers().getIndex(layer));
            }
        }

        int[] result = new int[indices.size];
        for (int i = 0; i < indices.size; i++) {
            result[i] = indices.get(i);
        }
        return result;
    }

    public void render(
        RenderContext context) {
        renderer.setView(
            context.getCamera());
    }
    public void renderGround(
        RenderContext context) {
        renderer.setView(
            context.getCamera());

        renderer.render(groundLayers);


    }

    public void renderAbovePlayer(
        RenderContext context) {
        renderer.setView(
            context.getCamera());


        renderer.render(foregroundLayers);

    }

    public void dispose() {
        renderer.dispose();
    }
}
