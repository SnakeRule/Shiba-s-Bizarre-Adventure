package com.shibe.game;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Jere on 18.8.2016.
 */
public class Enemy extends Character
{

    public Enemy(World world, RectangleMapObject rect, TiledMap map) {
        super(world, rect, map);
    }
}
