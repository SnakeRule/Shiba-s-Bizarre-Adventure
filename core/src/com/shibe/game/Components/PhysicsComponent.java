package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Jere on 26.8.2016.
 */
public class PhysicsComponent implements Component
{
    public Body body;

    public void setBody(Body b)
    {
        body = b;
    }
}
