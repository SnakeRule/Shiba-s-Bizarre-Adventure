package com.shibe.game.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.shibe.game.Components.CursorComponent;
import com.shibe.game.Components.SpriteComponent;
import com.shibe.game.Systems.CursorSystem;

import static com.shibe.game.Managers.Game.engine;

/**
 * Created by Jere on 30.8.2016.
 */
class CursorManager
{
    Texture aimReticleTex = new Texture("Crosshair.png");
    Sprite aimReticle = new Sprite(aimReticleTex);
    CursorComponent cursorComponent = new CursorComponent(aimReticle);
    Entity e = new Entity();
    SpriteComponent spriteComponent = new SpriteComponent();
    CursorSystem cursorSystem = new CursorSystem();

    public void initCursor()
    {
        aimReticle.setOrigin(0,0);
        aimReticle.setSize(aimReticle.getWidth() * Game.WORLD_TO_BOX / 25, aimReticle.getHeight() * Game.WORLD_TO_BOX / 25);

        e.add(cursorComponent);
        spriteComponent.setSprite(aimReticle);
        e.add(spriteComponent);
        engine.addEntity(e);
        engine.addSystem(cursorSystem);
    }
}
