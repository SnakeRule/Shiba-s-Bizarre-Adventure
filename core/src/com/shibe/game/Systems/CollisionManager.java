package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.*;

import java.util.ArrayList;


/**
 * Created by Jere on 27.8.2016.
 */
public class CollisionManager implements ContactListener
{
    private Engine engine;
    private World world;

    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
    private ComponentMapper<EnemyComponent> em = ComponentMapper.getFor(EnemyComponent.class);
    private ComponentMapper<ObjectComponent> om = ComponentMapper.getFor(ObjectComponent.class);
    private ComponentMapper<WeaponComponent> we = ComponentMapper.getFor(WeaponComponent.class);
    ImmutableArray<Entity> players;
    ImmutableArray<Entity> enemies;
    Entity collideeA = new Entity();
    Entity collideeB = new Entity();
    Entity e = new Entity();
    ArrayList<Fixture> fixtures = new ArrayList<Fixture>();


    public CollisionManager(Engine engine) {
        super();
        this.engine = engine;

        ImmutableArray<Entity> worlds = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
        for(Entity entity:worlds) {
            WorldComponent world = wm.get(entity);
            this.world = world.world;
            world.world.setContactListener(this);
        }
    }

    private void CreateCollision(int type, Fixture a, Fixture b)
    {
        switch (type)
        {
            case 1:
            {
                if(a.getUserData() == "PlayerManager") {
                    PlayerComponent player = pm.get((Entity) a.getBody().getUserData());
                    //player.body.applyForceToCenter(player.body.getLinearVelocity().x*-100, player.body.getLinearVelocity().y*-100, true);
                }
                else
                {
                    PlayerComponent player = pm.get((Entity) b.getBody().getUserData());
                    //player.body.applyForceToCenter(player.body.getLinearVelocity().x*-100, player.body.getLinearVelocity().y*-100, true);
                }
                break;
            }
            case 2:
            {
                if(a.getUserData() == "PlayerFeet")
                {
                    PlayerComponent player = pm.get((Entity) a.getBody().getUserData());
                    player.feetCollisions ++;
                }
                if(b.getUserData() == "PlayerFeet")
                {
                    PlayerComponent player = pm.get((Entity) b.getBody().getUserData());
                    player.feetCollisions ++;
                }
                break;
            }
            case 3:
            {
                if(a.getUserData() == "Button")
                {
                    ObjectComponent object = om.get((Entity) a.getBody().getUserData());
                    {
                        object.DoorMove = true;
                    }
                }
                if(b.getUserData() == "Button")
                {
                    ObjectComponent object = om.get((Entity) b.getBody().getUserData());
                    {
                        object.DoorMove = true;
                    }
                }
                break;
            }
            case 4:
            {
                PlayerComponent player = pm.get((Entity) a.getBody().getUserData());
                player.TouchingLadder = true;
                break;
            }
            case 5:
            {
                EnemyComponent enemy = em.get((Entity) b.getBody().getUserData());
                WeaponComponent weapon = we.get((Entity) a.getBody().getUserData());
                if(weapon.Owner == "PlayerManager" && weapon.type != 1) {
                    enemy.health -= weapon.Damage;
                    weapon.Delete = true;
                }
                break;
            }
            case 6:
            {
                EnemyComponent enemy = em.get((Entity) a.getBody().getUserData());
                PlayerComponent player = pm.get((Entity) b.getBody().getUserData());
                enemy.moveLeft = true;
                break;
            }
            case 7:
            {
                EnemyComponent enemy = em.get((Entity) a.getBody().getUserData());
                PlayerComponent player = pm.get((Entity) b.getBody().getUserData());
                enemy.moveRight = true;
                break;
            }
            case 8:
            {
                if(a.getUserData() == "Weapon" && !b.isSensor())
                {
                    WeaponComponent weapon = we.get((Entity) a.getBody().getUserData());
                    if(weapon.type != 1)
                        weapon.Delete = true;
                }
                else if(b.getUserData() == "Weapon" && !a.isSensor())
                {
                    WeaponComponent weapon = we.get((Entity) b.getBody().getUserData());
                    if(weapon.type != 1)
                        weapon.Delete = true;
                }
                break;
            }
            case 9:
            {
                if(a.getUserData() == "Weapon")
                {
                    WeaponComponent weapon = we.get((Entity) a.getBody().getUserData());
                    if(weapon.type != 1)
                        weapon.Delete = true;
                }
                if(b.getUserData() == "Weapon")
                {
                    WeaponComponent weapon = we.get((Entity) b.getBody().getUserData());
                    if(weapon.type != 1)
                        weapon.Delete = true;
                }
            }
        }
    }

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().getUserData() == "PlayerManager" && contact.getFixtureA().isSensor() == false)
        {
            if(contact.getFixtureB().getUserData() == "EnemyManager" && contact.getFixtureB().isSensor() == false)
                CreateCollision(1, contact.getFixtureA(), contact.getFixtureB());
        }
        else if(contact.getFixtureA().getUserData() == "PlayerFeet" || contact.getFixtureB().getUserData() == "PlayerFeet")
        {
            CreateCollision(2, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureA().getUserData() == "Button" || contact.getFixtureB().getUserData() == "Button")
        {
            CreateCollision(3, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureA().getUserData() == "PlayerManager" && contact.getFixtureB().getUserData() == "Ladder")
        {
            CreateCollision(4, contact.getFixtureA(),contact.getFixtureB());
        }
        else if(contact.getFixtureB().getUserData() == "PlayerManager" && contact.getFixtureA().getUserData() == "Ladder")
        {
            CreateCollision(4, contact.getFixtureB(),contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Weapon" && contact.getFixtureB().getUserData() == "EnemyManager")
        {
            CreateCollision(5, contact.getFixtureA(), contact.getFixtureB());
        }
        else if(contact.getFixtureB().getUserData() == "Weapon" && contact.getFixtureA().getUserData() == "EnemyManager")
        {
            CreateCollision(5, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Left" && contact.getFixtureB().getUserData() == "PlayerManager")
        {
            CreateCollision(6, contact.getFixtureA(), contact.getFixtureB());
        }
        else if(contact.getFixtureB().getUserData() == "Left" && contact.getFixtureA().getUserData() == "PlayerManager")
        {
            CreateCollision(6, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Right" && contact.getFixtureB().getUserData() == "PlayerManager")
        {
            CreateCollision(7, contact.getFixtureA(), contact.getFixtureB());
        }
        else if(contact.getFixtureB().getUserData() == "Right" && contact.getFixtureA().getUserData() == "PlayerManager")
        {
            CreateCollision(7, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Weapon" || contact.getFixtureB().getUserData() == "Weapon")
        {
            CreateCollision(8, contact.getFixtureA(),contact.getFixtureB());
        }
        if(contact.getFixtureA().getUserData() == "Weapon" && contact.getFixtureB().getUserData() == "Weapon")
        {
            CreateCollision(9, contact.getFixtureA(),contact.getFixtureB());
        }
    }

    @Override
    public void endContact(Contact contact) {
        if(contact.getFixtureA().getUserData() == "PlayerFeet")
        {
            PlayerComponent player = pm.get((Entity) contact.getFixtureA().getBody().getUserData());
            player.feetCollisions --;
        }
        else if (contact.getFixtureB().getUserData() == "PlayerFeet")
        {
            PlayerComponent player = pm.get((Entity) contact.getFixtureB().getBody().getUserData());
            player.feetCollisions --;
        }

        if(contact.getFixtureA().getUserData() == "PlayerManager" && contact.getFixtureB().getUserData() == "Ladder")
        {
            PlayerComponent player = pm.get((Entity) contact.getFixtureA().getBody().getUserData());
            player.TouchingLadder = false;
        }
        else if(contact.getFixtureB().getUserData() == "PlayerManager" && contact.getFixtureA().getUserData() == "Ladder")
        {
            PlayerComponent player = pm.get((Entity) contact.getFixtureB().getBody().getUserData());
            player.TouchingLadder = false;
        }
        if(contact.getFixtureA().getUserData() == "Left" && contact.getFixtureB().getUserData() == "PlayerManager")
        {
            EnemyComponent enemy = em.get((Entity) contact.getFixtureA().getBody().getUserData());
            enemy.moveLeft = false;
        }
        else if(contact.getFixtureB().getUserData() == "Left" && contact.getFixtureA().getUserData() == "PlayerManager")
        {
            EnemyComponent enemy = em.get((Entity) contact.getFixtureB().getBody().getUserData());
            enemy.moveLeft = false;
        }
        if(contact.getFixtureA().getUserData() == "Right" && contact.getFixtureB().getUserData() == "PlayerManager")
        {
            EnemyComponent enemy = em.get((Entity) contact.getFixtureA().getBody().getUserData());
            enemy.moveRight = false;
        }
        else if(contact.getFixtureB().getUserData() == "Right" && contact.getFixtureA().getUserData() == "PlayerManager")
        {
            EnemyComponent enemy = em.get((Entity) contact.getFixtureB().getBody().getUserData());
            enemy.moveRight = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
