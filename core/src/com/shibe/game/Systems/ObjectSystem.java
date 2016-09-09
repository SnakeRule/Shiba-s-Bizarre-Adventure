package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.shibe.game.Components.ObjectComponent;

/**
 * Created by Jere on 30.8.2016.
 */
public class ObjectSystem extends EntitySystem {
    private ComponentMapper<ObjectComponent> om = ComponentMapper.getFor(ObjectComponent.class);
    private ImmutableArray<Entity> objects;

    public ObjectSystem() {

        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        objects = engine.getEntitiesFor(Family.all(ObjectComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (int i = 0; i < objects.size(); ++i) {
            Entity entity = objects.get(i);
            ObjectComponent object = om.get(entity);

            if(object.DoorMove == true)
            {
                if (!object.DoorOpen && object.DoorMove == true) {
                    OpenDoor(object.Nmb,object.DoorMove);
                }
                object.DoorMove = false;
            }
            if(object.objectName.equals("Door") && object.body.getPosition().y >= object.SpawnY + 2)
            {
                object.body.setLinearVelocity(0,0);
                object.DoorOpen = true;
            }
            if(object.objectName.equals("Door") && object.body.getPosition().y <= object.SpawnY)
            {
                object.body.setLinearVelocity(0,0);
                object.DoorOpen = false;
            }
        }
    }
    private void OpenDoor(String Nmb, boolean DoorMove)
    {
        for (int i = 0; i < objects.size(); ++i) {
            Entity entity = objects.get(i);
            final ObjectComponent object = om.get(entity);

            if(object.objectName.equals("Door") == true && object.Nmb.equals(Nmb) == true && object.DoorOpen == false)
            {
                object.body.setLinearVelocity(0,10);
            }
            if(object.objectName.equals("Door") == true && object.Nmb.equals(Nmb) == true && object.DoorOpen == true)
            {
                object.body.setLinearVelocity(0,-10);
            }
        }
    }
}

