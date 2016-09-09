package com.shibe.game.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.shibe.game.Components.CursorComponent;
import com.shibe.game.Components.SpriteComponent;
import com.shibe.game.Systems.CursorSystem;

/**
 * Created by Jere on 30.8.2016.
 */
class CursorManager
{

    public CursorManager(Engine engine)
    {
        Texture aimReticleTex = new Texture("Crosshair.png");
        Sprite aimReticle = new Sprite(aimReticleTex);
        aimReticle.setOrigin(0,0);
        aimReticle.setSize((aimReticle.getWidth() / 15)* Game.WORLD_TO_BOX,(aimReticle.getHeight()/15)* Game.WORLD_TO_BOX);

        CursorComponent cursorComponent = new CursorComponent(aimReticle);
        Entity e = new Entity();
        e.add(cursorComponent);
        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.setSprite(aimReticle);
        e.add(spriteComponent);
        engine.addEntity(e);
        CursorSystem cursorSystem = new CursorSystem();
        engine.addSystem(cursorSystem);
    }
}
