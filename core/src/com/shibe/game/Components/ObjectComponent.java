package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Jere on 30.8.2016.
 */
public class ObjectComponent implements Component
{
    public String Nmb;
    public String objectName;
    public boolean DoorOpen;
    public boolean DoorMove;
    public Body body;
    public float SpawnY;

    public ObjectComponent()
    {
    }
    public void setSprite(Sprite sprite)
    {
        Sprite sprite1 = sprite;
    }
    public void setNmb(String nmb)
    {
        Nmb = nmb;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    public void setSpawnY(float SpawnY)
    {
        this.SpawnY = SpawnY;
    }
}
