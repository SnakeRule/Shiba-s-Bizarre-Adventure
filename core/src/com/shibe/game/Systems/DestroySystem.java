package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.shibe.game.Components.PlayerComponent;
import com.shibe.game.Components.RenderComponent;
import com.shibe.game.Components.WorldComponent;
import com.shibe.game.Managers.ActionProcessor;
import com.shibe.game.Managers.Game;
import com.shibe.game.MenuStage;

import java.util.ArrayList;

/**
 * Created by Jere on 31.8.2016.
 */
public class DestroySystem extends EntitySystem {

    private int goalTimer;
    public static ArrayList<Body> BodyDestroyList = new ArrayList<Body>();
    public static ArrayList<Entity> EntityDestroyList = new ArrayList<Entity>();
    public static ArrayList<Texture> TextureDestroyList = new ArrayList<Texture>();
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
    private ImmutableArray<Entity> worlds;
    private Array<Body> bodies = new Array<Body>();

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

        if(Game.Goal)
        {
            goalTimer++;

            if(goalTimer > 150)
            {
                DestroyMap(world);
                goalTimer = 0;
                Game.Goal = false;
            }
        }
        if(Game.Quit)
        {
            MenuStage.startButton.setText("Start Game");
            DestroyMap(world);
            Game.Quit = false;
        }
    }

    private void DestroyMap(WorldComponent world)
    {
        Game.pause = true;
        Game.Menu = true;
        Game.Load = true;
        Engine engine = getEngine();
        world.world.getBodies(bodies);
        engine.removeAllEntities();
        for(int i = 0; i < bodies.size;i++)
        {
            world.world.destroyBody(bodies.get(i));
        }
        RenderComponent.renderer.dispose();
        RenderComponent.batch.dispose();
        bodies.clear();
        Game.gameOn = false;
        ActionProcessor.ResetControls();
    }
}
