package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Jere on 27.8.2016.
 */
class CollisionComponent implements Component
{
    private ContactListener contactListener;
    private Body bodyA;
    private Body bodyB;
    private boolean collision;

    public void setContactListener(ContactListener c)
    {
        contactListener = c;

        contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact)
            {
                bodyA = contact.getFixtureA().getBody();
                bodyB = contact.getFixtureB().getBody();

                collision = true;
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
    }
}
