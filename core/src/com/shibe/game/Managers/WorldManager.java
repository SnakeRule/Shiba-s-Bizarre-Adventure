package com.shibe.game.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.shibe.game.Components.WorldComponent;

/**
 * Created by Jere on 30.8.2016.
 */
class WorldManager
{

    public WorldManager(Engine engine)
    {
        //Box2D world creation
        World world = new World(new Vector2(0, -13), true);

        //Box2D renderer
        //Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

        WorldComponent worldComponent = new WorldComponent();
        worldComponent.setWorld(world);
        Entity worldEntity = new Entity();
        worldEntity.add(worldComponent);
        engine.addEntity(worldEntity);
        worldEntity.add(worldComponent);
    }
}
