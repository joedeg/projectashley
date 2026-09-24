package com.jdegnan.projectashley;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class MapCollisionData {

    private final float tileSize;

    private final Array<Rectangle> collisionRects = new Array<>();

    private Rectangle mapBounds;

    public MapCollisionData(
        TiledMap  map,
        float tileSize){
        this.tileSize = tileSize;

        buildFromMap(map);
    }

    private void buildFromMap(TiledMap map) {

        TiledMapTileLayer layer =
            (TiledMapTileLayer) map.getLayers().get("Collision");

        if (layer == null) {
            throw new IllegalStateException(
                "Collision layer missing!"

            );


        }

        mapBounds = new Rectangle(
            0,
            0,
            layer.getWidth() * tileSize,
            layer.getHeight() * tileSize
        );


        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                if (cell == null) {
                    continue;
                }

                Rectangle rect = new Rectangle(
                    x * tileSize,
                    y * tileSize,
                    tileSize,
                    tileSize
                );

                collisionRects.add(rect);
            }
        }
    }

    public Array<Rectangle> getCollisionRects(){
        return collisionRects;
    }

    public Rectangle getMapBounds(){
        return mapBounds;
    }
}
