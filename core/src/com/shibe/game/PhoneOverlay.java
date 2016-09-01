package com.shibe.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.shibe.game.Managers.Game;

/**
 * Created by Jere on 22.8.2016.
 */
public class PhoneOverlay
{
    private Texture buttonImage;
    public Sprite ArrowUp;
    public Sprite ArrowRight;
    public Sprite ArrowDown;
    public Sprite ArrowLeft;

    public PhoneOverlay()
    {
        buttonImage = new Texture("TouchButtonUp.png");
        ArrowUp = new Sprite(buttonImage);
        ArrowUp.setSize(ArrowUp.getWidth()* Game.WORLD_TO_BOX, ArrowUp.getHeight()*Game.WORLD_TO_BOX);
        ArrowUp.setPosition(100*Game.WORLD_TO_BOX, 150*Game.WORLD_TO_BOX);
    }
}
