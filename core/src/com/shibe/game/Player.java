package com.shibe.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

/**
 * Created by Jere on 18.8.2016.
 */
public class Player extends Character {
    public Sprite AimReticle;

    public Player(World world, RectangleMapObject rect, TiledMap map) {
        super(world, rect, map);


        PolygonShape dogeFeetBox = new PolygonShape();
        dogeFeetBox.setAsBox((float) (dogeSprite.getWidth() - 0.54), (float) 0.05, new Vector2(0, (float) -0.375), 0);

        FixtureDef feetFixtureDef = new FixtureDef();
        feetFixtureDef.isSensor = true;
        feetFixtureDef.shape = dogeFeetBox;
        feetFixtureDef.filter.groupIndex = -8;

        dogeBody.createFixture(feetFixtureDef);

        Texture aimReticleTex = new Texture("Crosshair.png");
        AimReticle = new Sprite(aimReticleTex);
        AimReticle.setOrigin(0,0);
        AimReticle.setSize((AimReticle.getWidth() / 15)* Game.WORLD_TO_BOX,(AimReticle.getHeight()/15)* Game.WORLD_TO_BOX);
    }

    public void Shoot(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double width, double height, int weaponNmb, Body dogeBody)
    {
        weapon = new Weapon(world, charX, charY, pointerX, pointerY, flipX, width, height, weaponNmb, dogeBody);
        weapons.add(weapon);
    }
}
