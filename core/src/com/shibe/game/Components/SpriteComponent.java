package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Jere on 26.8.2016.
 */
public class SpriteComponent implements Component
{
    public Sprite sprite;
    public void setSprite(Sprite s){
        sprite = s;
    }
}
