package com.jdegnan.projectashley.factories;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.BrazierAssets;
import com.jdegnan.projectashley.components.BrazierAnimationComponent;
import com.jdegnan.projectashley.components.BrazierComponent;
import com.jdegnan.projectashley.components.BrazierGateComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.level.LevelEntityFactory;
import com.jdegnan.projectashley.level.LevelObjectData;

public class BrazierFactory implements LevelEntityFactory {

    private final Assets assets;

    public BrazierFactory(Assets assets) {
        this.assets = assets;

    }

    @Override
    public Entity create(LevelObjectData object) {
        Entity entity = new Entity();

        Vector2 position = object.getPosition();

        PositionComponent positionComponent =
            new PositionComponent();
        positionComponent.x = position.x;
        positionComponent.y = position.y;

        entity.add(positionComponent);

        BrazierComponent brazier = new BrazierComponent();
        entity.add(brazier);

        brazier.puzzleId = object.getStringProperty("puzzleId", null);

        BrazierAnimationComponent animation =
            new BrazierAnimationComponent();

        Texture texture = assets.get(BrazierAssets.BRAZIER);

        animation.unlitFrame =
            new TextureRegion(texture, 0, 0, 32, 32);

        animation.litFrames = new TextureRegion[]{
            new TextureRegion(texture, 32, 0, 32, 32),
            new TextureRegion(texture, 64, 0, 32, 32),
            new TextureRegion(texture, 96, 0, 32, 32)
        };

        entity.add(animation);

        RenderComponent renderComponent = new RenderComponent();

        renderComponent.region = animation.unlitFrame;
        renderComponent.width = 32;
        renderComponent.height = 32f;

        entity.add(renderComponent);

        return entity;


    }
}
