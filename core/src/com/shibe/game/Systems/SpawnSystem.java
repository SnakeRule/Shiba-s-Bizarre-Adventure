package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.shibe.game.Components.SpawnComponent;
import com.shibe.game.Components.WorldComponent;
import com.shibe.game.Managers.EnemyManager;
import com.shibe.game.Managers.Game;

/**
 * Created by Jere on 8.9.2016.
 */
public class SpawnSystem extends EntitySystem
{
    private ComponentMapper<SpawnComponent> sm = ComponentMapper.getFor(SpawnComponent.class);
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
    private ImmutableArray<Entity> spawns;
    private ImmutableArray<Entity> worlds;
    private SpawnComponent spawn;
    private EnemyManager enemyManager;

    public SpawnSystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        spawns = engine.getEntitiesFor(Family.all(SpawnComponent.class).get());
        worlds = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
        enemyManager = new EnemyManager();
    }

    @Override
    public void update(float deltaTime) {
        if (!Game.pause) {
            super.update(deltaTime);
            Entity e = worlds.first();
            WorldComponent world = wm.get(e);
            for (int i = 0; i < spawns.size(); i++) {
                e = spawns.get(i);
                spawn = sm.get(e);

                if (spawn.spawn == true) {
                    enemyManager.createEnemy(world.world, spawn.spawnX, spawn.spawnY, spawn.spawnType);
                    spawn.spawn = false;
                }
            }
        }
    }
}
