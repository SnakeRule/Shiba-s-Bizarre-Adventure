package com.shibe.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Created by Jere on 18.8.2016.
 */
public class Player extends Character {
    public Sprite AimReticle;

    public Player(World world, RectangleMapObject rect, TiledMap map) {
        super(world, rect, map);
        Texture aimReticleTex = new Texture("Crosshair.png");
        AimReticle = new Sprite(aimReticleTex);
        AimReticle.setOrigin(0,0);
        AimReticle.setSize((AimReticle.getWidth() / 15)* Game.WORLD_TO_BOX,(AimReticle.getHeight()/15)* Game.WORLD_TO_BOX);

    }

    public void Shoot(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double width, double height, int weaponNmb)
    {
        weapon = new Weapon(world, charX, charY, pointerX, pointerY, flipX, width, height, weaponNmb);
        weapons.add(weapon);
    }
}
