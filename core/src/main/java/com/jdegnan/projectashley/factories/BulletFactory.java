package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;
import com.jdegnan.projectashley.components.DamageComponent;
import com.jdegnan.projectashley.components.LifetimeComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.SpriteComponent;
import com.jdegnan.projectashley.components.VelocityComponent;

public class BulletFactory {
    private PooledEngine engine;
    private TextureAtlas atlas;

    public BulletFactory(PooledEngine engine, TextureAtlas atlas) {
        this.engine = engine;
        this.atlas = atlas;
    }

    public Entity create(float x,
                         float y,
                         float vx,
                         float vy) {
        Entity bullet = engine.createEntity();

        PositionComponent pos = engine.createComponent(
            PositionComponent.class
        );

        pos.x = x;
        pos.y = y;

        VelocityComponent vel = engine.createComponent(
            VelocityComponent.class
        );

        vel.vx = vx;
        vel.vy = vy;

        DamageComponent dmg = engine.createComponent(
            DamageComponent.class
        );

        dmg.damage = 10;

        LifetimeComponent life = engine.createComponent(
            LifetimeComponent.class
        );

        life.timer = 2f;

        SpriteComponent spr = engine.createComponent(
            SpriteComponent.class
        );

        spr.textureRegion = atlas.findRegion("bullet");

        RenderComponent render = engine.createComponent(
            RenderComponent.class
        );

        render.region = spr.textureRegion;
        render.width = 16;
        render.height = 16;
        render.layer = 1;

        bullet.add(pos);
        bullet.add(vel);
        bullet.add(dmg);
        bullet.add(life);
        bullet.add(spr);
        bullet.add(render);

        engine.addEntity(bullet);

        return bullet;
    }
}

