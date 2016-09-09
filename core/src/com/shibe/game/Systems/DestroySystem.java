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
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
    private ImmutableArray<Entity> worlds;

    public DestroySystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        worlds = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
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
            EntityDestroyList.clear();
        }
        BodyDestroyList.clear();
        super.update(deltaTime);
    }
}
