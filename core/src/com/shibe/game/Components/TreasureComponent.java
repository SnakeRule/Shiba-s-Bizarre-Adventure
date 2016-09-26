package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Jere on 16.9.2016.
 */
public class TreasureComponent implements Component
{
    public boolean Open;
    public boolean Opened;
    public int Type;
    public Sprite sprite;
    public Texture closed;
    public Texture openTexture;

    public void setType(int type) {
        Type = type;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setClosed(Texture closed) {
        this.closed = closed;
    }

    public void setOpened(Texture open) {
        this.openTexture = open;
    }
}
