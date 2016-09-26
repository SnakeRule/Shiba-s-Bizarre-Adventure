package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Jere on 16.9.2016.
 */
public class ItemComponent implements Component
{
    public int moneyValue;
    public int itemID;
    public int healthRestored;
    public int bullets;
    public boolean CanPickUp;
    public Body body;
    public Sprite sprite;

    public ItemComponent()
    {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                CanPickUp = true;
            }
        }, (float) 0.5,0,0
        );
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setHealthRestored(int healthRestored) {
        this.healthRestored = healthRestored;
    }

    public void setMoneyValue(int moneyValue) {
        this.moneyValue = moneyValue;
    }
}
