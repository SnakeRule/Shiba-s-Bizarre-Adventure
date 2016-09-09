package com.shibe.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.shibe.game.Managers.Game;

/**
 * Created by Jere on 22.8.2016.
 */
public class PhoneOverlay
{
    public Sprite ArrowRight;
    public Sprite ArrowDown;
    public Sprite ArrowLeft;

    public PhoneOverlay()
    {
        Texture buttonImage = new Texture("TouchButtonUp.png");
        Sprite arrowUp = new Sprite(buttonImage);
        arrowUp.setSize(arrowUp.getWidth()* Game.WORLD_TO_BOX, arrowUp.getHeight()*Game.WORLD_TO_BOX);
        arrowUp.setPosition(100*Game.WORLD_TO_BOX, 150*Game.WORLD_TO_BOX);
    }
}
