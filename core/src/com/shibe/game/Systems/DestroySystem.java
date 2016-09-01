package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.shibe.game.Components.PlayerComponent;
import com.shibe.game.Components.WeaponComponent;
import com.shibe.game.Components.WorldComponent;
import com.shibe.game.Managers.Game;

import java.util.ArrayList;

/**
 * Created by Jere on 31.8.2016.
 */
public class DestroySystem extends EntitySystem {

    public static ArrayList<Body> BodyDestroyList = new ArrayList<Body>();
    public static ArrayList<Entity> EntityDestroyList = new ArrayList<Entity>();
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ImmutableArray<Entity> players;
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
    private ImmutableArray<Entity> worlds;
    private ComponentMapper<WeaponComponent> we = ComponentMapper.getFor(WeaponComponent.class);
    private ImmutableArray<Entity> weapons;

    public DestroySystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        worlds = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
        weapons = engine.getEntitiesFor(Family.all(WeaponComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        Entity e = players.get(0);
        PlayerComponent player = pm.get(e);
        Entity w = worlds.get(0);
        WorldComponent world = wm.get(w);

        if (BodyDestroyList.isEmpty() == false) {
            for (Body body : BodyDestroyList) {
                world.world.destroyBody(body);
            }
        }
        if(!EntityDestroyList.isEmpty())
        {
            Engine engine = getEngine();
            for(Entity entity : EntityDestroyList)
            {
                engine.removeEntity(entity);
            }
        }

        if (player.weapons.size() > 10) {
            Game.engine.removeEntity(player.weapons.get(0).e);
            world.world.destroyBody(player.weapons.get(0).weaponBody);
            player.weapons.remove(0);
        }
        BodyDestroyList.clear();
        super.update(deltaTime);
    }
}
