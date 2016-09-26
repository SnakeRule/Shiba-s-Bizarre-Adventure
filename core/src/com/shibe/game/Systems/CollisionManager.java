package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.*;
import com.shibe.game.HudStage;
import com.shibe.game.Managers.*;

import java.util.ArrayList;


/**
 * Created by Jere on 27.8.2016.
 */
public class CollisionManager implements ContactListener
{

    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<EnemyComponent> em = ComponentMapper.getFor(EnemyComponent.class);
    private ComponentMapper<ObjectComponent> om = ComponentMapper.getFor(ObjectComponent.class);
    private ComponentMapper<WeaponComponent> we = ComponentMapper.getFor(WeaponComponent.class);
    private ComponentMapper<SpawnComponent> sm = ComponentMapper.getFor(SpawnComponent.class);
    private ComponentMapper<ItemComponent> im = ComponentMapper.getFor(ItemComponent.class);
    private ComponentMapper<TreasureComponent> tm = ComponentMapper.getFor(TreasureComponent.class);
    private PlayerComponent player;
    private EnemyComponent enemy;
    private WeaponComponent weapon;
    private ObjectComponent object;
    private SpawnComponent spawn;
    private ItemComponent item;
    private TreasureComponent treasure;
    private WorldComponent world;
    ImmutableArray<Entity> spawns;
    ImmutableArray<Entity> players;
    ImmutableArray<Entity> enemies;
    Entity e = new Entity();
    ArrayList<Fixture> fixtures = new ArrayList<Fixture>();
    ItemManager itemManager = new ItemManager();


    public CollisionManager(Engine engine) {
        super();

        ImmutableArray<Entity> worlds = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
        for(Entity entity:worlds) {
            ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
            WorldComponent world = wm.get(entity);
            world.world.setContactListener(this);
        }
        spawns = engine.getEntitiesFor(Family.all(SpawnComponent.class).get());
    }

    private void CreateCollision(int type, Fixture a, Fixture b)
    {
        switch (type)
        {
            case 1:
            {
                if(a.getUserData() == "Player") {
                    player = pm.get((Entity) a.getBody().getUserData());
                    //player.body.applyForceToCenter(player.body.getLinearVelocity().x*-100, player.body.getLinearVelocity().y*-100, true);
                }
                else
                {
                    player = pm.get((Entity) b.getBody().getUserData());
                    //player.body.applyForceToCenter(player.body.getLinearVelocity().x*-100, player.body.getLinearVelocity().y*-100, true);
                }
                break;
            }
            case 2:
            {
                if(a.getUserData() == "PlayerFeet")
                {
                    player = pm.get((Entity) a.getBody().getUserData());
                    player.feetCollisions ++;
                }
                if(b.getUserData() == "PlayerFeet")
                {
                    player = pm.get((Entity) b.getBody().getUserData());
                    player.feetCollisions ++;
                }
                break;
            }
            case 3:
            {
                if(a.getUserData() == "Button")
                {
                    object = om.get((Entity) a.getBody().getUserData());
                    {
                        object.DoorMove = true;
                    }
                }
                if(b.getUserData() == "Button")
                {
                    object = om.get((Entity) b.getBody().getUserData());
                    {
                        object.DoorMove = true;
                    }
                }
                break;
            }
            case 4:
            {
                player = pm.get((Entity) a.getBody().getUserData());
                player.TouchingLadder = true;
                break;
            }
            case 5:
            {
                enemy = em.get((Entity) b.getBody().getUserData());
                weapon = we.get((Entity) a.getBody().getUserData());
                if(weapon.Owner == "Player" && weapon.type != 1) {
                    enemy.health -= weapon.Damage;
                    enemy.body.applyForceToCenter(weapon.body.getLinearVelocity().x, weapon.body.getLinearVelocity().y, true);
                    weapon.Delete = true;
                }
                break;
            }
            case 6:
            {
                enemy = em.get((Entity) a.getBody().getUserData());
                player = pm.get((Entity) b.getBody().getUserData());
                enemy.moveLeft = true;
                enemy.PlayerSpotted = true;
                break;
            }
            case 7:
            {
                enemy = em.get((Entity) a.getBody().getUserData());
                player = pm.get((Entity) b.getBody().getUserData());
                enemy.moveRight = true;
                enemy.PlayerSpotted = true;
                break;
            }
            case 8:
            {
                if(a.getUserData() == "Weapon" && !b.isSensor())
                {
                    weapon = we.get((Entity) a.getBody().getUserData());
                    if(weapon.type != 1)
                        weapon.Delete = true;
                }
                else if(b.getUserData() == "Weapon" && !a.isSensor())
                {
                    weapon = we.get((Entity) b.getBody().getUserData());
                    if(weapon.type != 1)
                        weapon.Delete = true;
                }
                break;
            }
            case 10:
            {
                int spawntriggerLink = Integer.parseInt(b.getUserData().toString());
                for(int i = 0; i < spawns.size(); i++)
                {
                    Entity e;
                    e = spawns.get(i);
                    spawn = sm.get(e);

                    if(spawn.spawnLink == spawntriggerLink)
                    {
                        spawn.spawn = true;
                    }
                }
                DestroySystem.BodyDestroyList.add(b.getBody());
                break;
            }
            case 11:
            {
                item = im.get((Entity) b.getUserData());
                player = pm.get((Entity) a.getBody().getUserData());
                if(item.CanPickUp)
                {
                    switch (item.itemID)
                    {
                        case 1:
                        {
                            player.weapons.put("Missile", player.weapons.get("Missile") + 10);
                            break;
                        }
                        case 2:case 3:
                        {
                            player.hp += item.healthRestored;
                            if(player.hp > player.maxhp)
                                player.hp = player.maxhp;
                            break;
                        }
                        case 4:
                        {
                            player.money += item.moneyValue;
                            break;
                        }
                    }

                    DestroySystem.BodyDestroyList.add(b.getBody());
                    DestroySystem.EntityDestroyList.add((Entity) b.getUserData());
                }
                break;
            }
            case 12:
            {
                treasure = tm.get((Entity) b.getBody().getUserData());

                treasure.Open = true;

                break;
            }
            case 13:
            {
                object = om.get((Entity) b.getBody().getUserData());
                object.TeleportReady = true;
                break;
            }
            case 14:
            {
                object = om.get((Entity) b.getBody().getUserData());
                weapon = we.get((Entity) a.getBody().getUserData());
                if(object.TeleportReady && weapon.Owner == "Player")
                    object.Teleport = true;
                break;
            }
            case 15:
            {
                if(!ActionProcessor.GodMode) {
                    player = pm.get((Entity) b.getBody().getUserData());
                    weapon = we.get((Entity) a.getBody().getUserData());
                    if (weapon.Owner == "Enemy") {
                        player.hp -= weapon.Damage;
                        player.body.applyForceToCenter(weapon.body.getLinearVelocity().x, weapon.body.getLinearVelocity().y, true);
                        weapon.Delete = true;
                    }
                }
                break;
            }
            case 16:
            {
                Game.Goal = true;
                if(Level.levelNmb > player.levelsUnlocked)
                    player.levelsUnlocked++;

                SaveSystem.playerData.hp = player.hp;
                SaveSystem.playerData.maxhp = player.maxhp;
                SaveSystem.playerData.money = player.money;
                SaveSystem.playerData.levelsCompleted = player.levelsUnlocked;
                SaveSystem.playerData.name = HudStage.Name.getText().toString();

                Game.Save = true;
                break;
            }
            case 17:
            {
                weapon = we.get((Entity) b.getBody().getUserData());
                weapon.Delete = true;
                break;
            }
        }
    }

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().getUserData() == "Player" && contact.getFixtureA().isSensor() == false)
        {
            if(contact.getFixtureB().getUserData() == "Enemy" && contact.getFixtureB().isSensor() == false)
                CreateCollision(1, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureA().getUserData() == "PlayerFeet" || contact.getFixtureB().getUserData() == "PlayerFeet")
        {
            CreateCollision(2, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureA().getUserData() == "Button" || contact.getFixtureB().getUserData() == "Button")
        {
            CreateCollision(3, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureA().getUserData() == "Player" && contact.getFixtureB().getUserData() == "Ladder")
        {
            CreateCollision(4, contact.getFixtureA(),contact.getFixtureB());
        }
        else if(contact.getFixtureB().getUserData() == "Player" && contact.getFixtureA().getUserData() == "Ladder")
        {
            CreateCollision(4, contact.getFixtureB(),contact.getFixtureA());
        }
         if(contact.getFixtureA().getUserData() == "Weapon" && contact.getFixtureB().getUserData() == "Enemy")
        {
            CreateCollision(5, contact.getFixtureA(), contact.getFixtureB());
        }
        else if(contact.getFixtureB().getUserData() == "Weapon" && contact.getFixtureA().getUserData() == "Enemy")
        {
            CreateCollision(5, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Left" && contact.getFixtureB().getUserData() == "Player")
        {
            CreateCollision(6, contact.getFixtureA(), contact.getFixtureB());
        }
        else if(contact.getFixtureB().getUserData() == "Left" && contact.getFixtureA().getUserData() == "Player")
        {
            CreateCollision(6, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Right" && contact.getFixtureB().getUserData() == "Player")
        {
            CreateCollision(7, contact.getFixtureA(), contact.getFixtureB());
        }
        else if(contact.getFixtureB().getUserData() == "Right" && contact.getFixtureA().getUserData() == "Player")
        {
            CreateCollision(7, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Weapon" || contact.getFixtureB().getUserData() == "Weapon")
        {
            CreateCollision(8, contact.getFixtureA(),contact.getFixtureB());
        }
        if(contact.getFixtureA().getUserData() == "Player" && contact.getFixtureB().getBody().getUserData() == "SpawnTrigger")
        {
            CreateCollision(10, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureB().getUserData() == "Player" && contact.getFixtureA().getBody().getUserData() == "SpawnTrigger")
        {
            CreateCollision(10, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Player" && contact.getFixtureB().getBody().getUserData() == "Item")
        {
            CreateCollision(11, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureB().getUserData() == "Player" && contact.getFixtureA().getBody().getUserData() == "Item")
        {
            CreateCollision(11, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Player" && contact.getFixtureB().getUserData() == "Treasure")
        {
            CreateCollision(12, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureB().getUserData() == "Player" && contact.getFixtureA().getUserData() == "Treasure")
        {
            CreateCollision(12, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Player" && contact.getFixtureB().getUserData() == "Teleport")
        {
            CreateCollision(13, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureB().getUserData() == "Player" && contact.getFixtureA().getUserData() == "Teleport")
        {
            CreateCollision(13, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Weapon" && contact.getFixtureB().getUserData() == "Teleport")
        {
            CreateCollision(14, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureB().getUserData() == "Weapon" && contact.getFixtureA().getUserData() == "Teleport")
        {
            CreateCollision(14, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Weapon" && contact.getFixtureB().getUserData() == "Player")
        {
            CreateCollision(15, contact.getFixtureA(), contact.getFixtureB());
        }
        else if(contact.getFixtureB().getUserData() == "Weapon" && contact.getFixtureA().getUserData() == "Player")
        {
            CreateCollision(15, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getBody().getUserData() == "Goal" && contact.getFixtureB().getUserData() == "Player")
        {
            CreateCollision(16, contact.getFixtureA(), contact.getFixtureB());
        }
        else if(contact.getFixtureB().getBody().getUserData() == "Goal" && contact.getFixtureA().getUserData() == "Player")
        {
            CreateCollision(16, contact.getFixtureB(), contact.getFixtureA());
        }
        if(contact.getFixtureA().getUserData() == "Button" && contact.getFixtureB().getUserData() == "Weapon")
        {
            CreateCollision(17, contact.getFixtureA(), contact.getFixtureB());
        }
        if(contact.getFixtureA().getUserData() == "Weapon" && contact.getFixtureB().getUserData() == "Button")
        {
            CreateCollision(17, contact.getFixtureB(), contact.getFixtureA());
        }
    }

    @Override
    public void endContact(Contact contact) {
        if(contact.getFixtureA().getUserData() == "PlayerFeet")
        {
            player = pm.get((Entity) contact.getFixtureA().getBody().getUserData());
            player.feetCollisions --;
        }
        else if (contact.getFixtureB().getUserData() == "PlayerFeet")
        {
            player = pm.get((Entity) contact.getFixtureB().getBody().getUserData());
            player.feetCollisions --;
        }

        if(contact.getFixtureA().getUserData() == "Player" && contact.getFixtureB().getUserData() == "Ladder")
        {
            player = pm.get((Entity) contact.getFixtureA().getBody().getUserData());
            player.TouchingLadder = false;
        }
        else if(contact.getFixtureB().getUserData() == "Player" && contact.getFixtureA().getUserData() == "Ladder")
        {
            player = pm.get((Entity) contact.getFixtureB().getBody().getUserData());
            player.TouchingLadder = false;
        }
        if(contact.getFixtureA().getUserData() == "Left" && contact.getFixtureB().getUserData() == "Player")
        {
            enemy = em.get((Entity) contact.getFixtureA().getBody().getUserData());
            enemy.moveLeft = false;
            enemy.PlayerSpotted = false;
        }
        else if(contact.getFixtureB().getUserData() == "Left" && contact.getFixtureA().getUserData() == "Player")
        {
            enemy = em.get((Entity) contact.getFixtureB().getBody().getUserData());
            enemy.moveLeft = false;
            enemy.PlayerSpotted = false;
        }
        if(contact.getFixtureA().getUserData() == "Right" && contact.getFixtureB().getUserData() == "Player")
        {
            enemy = em.get((Entity) contact.getFixtureA().getBody().getUserData());
            enemy.moveRight = false;
            enemy.PlayerSpotted = false;
        }
        else if(contact.getFixtureB().getUserData() == "Right" && contact.getFixtureA().getUserData() == "Player")
        {
            enemy = em.get((Entity) contact.getFixtureB().getBody().getUserData());
            enemy.moveRight = false;
            enemy.PlayerSpotted = false;
        }
        if(contact.getFixtureA().getUserData() == "Player" && contact.getFixtureB().getUserData() == "Teleport")
        {
            object = om.get((Entity) contact.getFixtureB().getBody().getUserData());
            object.TeleportReady = false;
            object.Teleport = false;
        }
        if(contact.getFixtureB().getUserData() == "Player" && contact.getFixtureA().getUserData() == "Teleport")
        {
            object = om.get((Entity) contact.getFixtureA().getBody().getUserData());
            object.TeleportReady = false;
            object.Teleport = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
