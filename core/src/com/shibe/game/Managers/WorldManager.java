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
public class WorldManager
{
    private final World world;
    private final Box2DDebugRenderer debugRenderer;
    private final WorldComponent worldComponent = new WorldComponent();
    private final Entity worldEntity = new Entity();

    public WorldManager(Engine engine)
    {
        //Box2D world creation
        world = new World(new Vector2(0, -13), true);

        //Box2D renderer
        debugRenderer = new Box2DDebugRenderer();

        worldComponent.setWorld(world);
        worldEntity.add(worldComponent);
        engine.addEntity(worldEntity);
        worldEntity.add(worldComponent);
    }
}
