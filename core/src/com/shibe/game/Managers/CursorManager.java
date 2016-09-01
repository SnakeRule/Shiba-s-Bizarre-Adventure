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
    private Sprite AimReticle;
    private CursorComponent cursorComponent;
    private SpriteComponent spriteComponent;
    private Entity e = new Entity();

    public CursorManager(Engine engine)
    {
        Texture aimReticleTex = new Texture("Crosshair.png");
        AimReticle = new Sprite(aimReticleTex);
        AimReticle.setOrigin(0,0);
        AimReticle.setSize((AimReticle.getWidth() / 15)* Game.WORLD_TO_BOX,(AimReticle.getHeight()/15)* Game.WORLD_TO_BOX);

        cursorComponent = new CursorComponent(AimReticle);
        e.add(cursorComponent);
        spriteComponent = new SpriteComponent();
        spriteComponent.setSprite(AimReticle);
        e.add(spriteComponent);
        engine.addEntity(e);
        CursorSystem cursorSystem = new CursorSystem();
        engine.addSystem(cursorSystem);
    }
}
