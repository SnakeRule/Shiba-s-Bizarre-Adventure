package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Jere on 29.8.2016.
 */
public class WeaponComponent implements Component
{
    public int type;
    public int Damage;
    public boolean Delete;
    public Body body;
    public String Owner;
    public int timer = 0;

    public void setType(int t)
    {
        type = t;
    }
    public void setEntity(Entity e)
    {
        Entity entity = e;
    }
    public void setDamage(int damage)
    {
        Damage = damage;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setOwner(String owner)
    {
        Owner = owner;
    }
}
