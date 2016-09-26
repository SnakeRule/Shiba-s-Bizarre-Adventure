package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.shibe.game.Components.ObjectComponent;
import com.shibe.game.Components.PlayerComponent;
import com.shibe.game.Managers.Game;

/**
 * Created by Jere on 30.8.2016.
 */
public class ObjectSystem extends EntitySystem {
    private ComponentMapper<ObjectComponent> om = ComponentMapper.getFor(ObjectComponent.class);
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ImmutableArray<Entity> objects;
    private ImmutableArray<Entity> players;
    private Array<Fixture> fixtures;

    public ObjectSystem() {

        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        objects = engine.getEntitiesFor(Family.all(ObjectComponent.class).get());
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        if(!Game.pause) {
            super.update(deltaTime);
            for (int i = 0; i < objects.size(); ++i) {
                Entity entity = objects.get(i);
                ObjectComponent object = om.get(entity);

                if (object.DoorMove == true) {
                    if (!object.DoorOpen && object.DoorMove == true) {
                        OpenDoor(object.Nmb, object.DoorMove);
                    }
                    object.DoorMove = false;
                }
                if (object.objectName.equals("Door") && object.body.getPosition().y >= object.SpawnY + 3) {
                    object.body.setLinearVelocity(0, 0);
                    object.DoorOpen = true;
                }
                if (object.objectName.equals("Door") && object.body.getPosition().y <= object.SpawnY) {
                    object.body.setLinearVelocity(0, 0);
                    object.DoorOpen = false;
                }
                if (object.Teleport == true) {
                    Teleport(object);
                }
            }
        }
    }

    private void OpenDoor(String Nmb, boolean DoorMove) {
        for (int i = 0; i < objects.size(); ++i) {
            Entity entity = objects.get(i);
            ObjectComponent object = om.get(entity);

            if (object.objectName.equals("Door") == true && object.Nmb.equals(Nmb) == true && object.DoorOpen == false) {
                object.body.setLinearVelocity(0, 10);
                for(int o = 0; o < objects.size(); o++)
                {
                entity = objects.get(o);
                object = om.get(entity);
                    if(object.objectName.equals("Button") && object.Nmb.equals(Nmb))
                    {
                        object.sprite.setRegion(object.sprite.getU2()*2,0,object.sprite.getU2(),object.sprite.getV2());
                    }
                }
            }
            if (object.objectName.equals("Door") == true && object.Nmb.equals(Nmb) == true && object.DoorOpen == true) {
                object.body.setLinearVelocity(0, -10);
                for(int o = 0; o < objects.size(); o++)
                {
                    entity = objects.get(o);
                    object = om.get(entity);
                    if(object.objectName.equals("Button") && object.Nmb.equals(Nmb))
                    {
                        object.sprite.setRegion(0,0,object.sprite.getU2(),object.sprite.getV2());
                    }
                }
            }
        }
    }

    private void Teleport(ObjectComponent object) {
        String link;

        link = object.Nmb;
        for (int i = 0; i < objects.size(); ++i) {
            Entity entity = objects.get(i);
            object = om.get(entity);

            if (object.objectName.equals("Teleport") == true && object.Teleport == false && object.Nmb.equals(link))
            {
                Vector2 fug = new Vector2();
                Entity e = players.first();
                PlayerComponent player = pm.get(e);
                fixtures = object.body.getFixtureList();
                Fixture fixture = fixtures.first();
                PolygonShape shape = (PolygonShape) fixture.getShape();
                shape.getVertex(3, fug);
                player.body.setTransform(fug.x + player.sprite.getWidth() / 2,fug.y + player.sprite.getHeight()/2,0);
            }
        }
    }
}

