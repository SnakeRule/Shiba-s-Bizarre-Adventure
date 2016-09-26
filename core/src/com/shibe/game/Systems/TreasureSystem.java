package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.shibe.game.Components.TreasureComponent;
import com.shibe.game.Components.WorldComponent;
import com.shibe.game.Managers.Game;
import com.shibe.game.Managers.ItemManager;
import com.shibe.game.Managers.TreasureManager;

import java.util.Random;

/**
 * Created by Jere on 16.9.2016.
 */
public class TreasureSystem extends EntitySystem
{
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
    private ComponentMapper<TreasureComponent> tm = ComponentMapper.getFor(TreasureComponent.class);

    private TreasureComponent treasure;
    private WorldComponent world;
    private Random random = new Random();
    private int itemNmb;
    private ItemManager itemManager = new ItemManager();

    private Entity e;
    private Entity u;
    private ImmutableArray<Entity> treasures;
    private ImmutableArray<Entity> worlds;

    public TreasureSystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        treasures = engine.getEntitiesFor(Family.all(TreasureComponent.class).get());
        worlds = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        if (!Game.pause) {
            super.update(deltaTime);

            for (int i = 0; i < treasures.size(); i++) {
                e = treasures.get(i);
                treasure = tm.get(e);
                u = worlds.first();
                world = wm.get(u);

                if (treasure.Open && !treasure.Opened) {
                    treasure.sprite.setTexture(treasure.openTexture);

                    switch (treasure.Type) {
                        case 0: {
                            itemNmb = MathUtils.random(1, 5);
                            switch (itemNmb) {
                                case 1:
                                case 2:
                                case 3: {
                                    itemManager.createItem(itemNmb, treasure.sprite.getX() + treasure.sprite.getWidth() / 2, treasure.sprite.getY() + treasure.sprite.getHeight(), world.world);
                                    break;
                                }
                                case 4:
                                case 5:{
                                    for (int u = 0; u < MathUtils.random(20, 50); u++) {
                                        itemManager.createItem(itemNmb, treasure.sprite.getX() + treasure.sprite.getWidth() / 2, treasure.sprite.getY() + treasure.sprite.getHeight(), world.world);
                                    }
                                    break;
                                }
                            }
                            treasure.Opened = true;
                        }
                    }
                }
            }
        }
    }
}
