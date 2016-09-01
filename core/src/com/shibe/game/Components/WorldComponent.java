package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Jere on 27.8.2016.
 */
public class WorldComponent implements Component
{
    public World world;
    public Box2DDebugRenderer debugRenderer;

    public void setWorld(World w)
    {
        world = w;
        debugRenderer = new Box2DDebugRenderer();
    }

    public World returnWorld()
    {
        return world;
    }
}
