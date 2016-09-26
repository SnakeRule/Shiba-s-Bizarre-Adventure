package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.shibe.game.Systems.DestroySystem;

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
    private Timer weaponTimer;

    public void setType(int t)
    {
        type = t;
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

    public WeaponComponent()
    {
        weaponTimer = new Timer();
        weaponTimer.schedule(new Timer.Task() {
        @Override
        public void run() {
            Delete = true;
        }

    }, 8);
    }
}
