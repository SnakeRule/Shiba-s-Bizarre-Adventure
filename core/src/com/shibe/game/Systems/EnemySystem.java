package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.shibe.game.Components.EnemyComponent;
import com.shibe.game.Components.PlayerComponent;
import com.shibe.game.Components.WorldComponent;
import com.shibe.game.Managers.ActionProcessor;
import com.shibe.game.Managers.Game;
import com.shibe.game.Managers.ItemManager;
import com.shibe.game.Managers.WeaponManager;

import java.util.Random;

/**
 * Created by Jere on 30.8.2016.
 */
public class EnemySystem extends EntitySystem
{
    int dropChance;
    int dropType;
    private int timer = 0;
    private WeaponManager weaponManager;
    private ItemManager itemManager;
    private ComponentMapper<EnemyComponent> em = ComponentMapper.getFor(EnemyComponent.class);
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> worlds;
    private ImmutableArray<Entity> enemies;
    private WeaponManager weapon;

    public EnemySystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class).get());
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        worlds = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
        weaponManager = new WeaponManager();
        itemManager = new ItemManager();
    }

    @Override
    public void update(float deltaTime)
    {
        if(!Game.pause) {
            super.update(deltaTime);
            for (int i = 0; i < enemies.size(); i++) {
                Entity e = enemies.get(i);
                EnemyComponent enemy = em.get(e);
                Entity u = worlds.first();
                WorldComponent world = wm.get(u);

                if (enemy.health <= 0) {
                    ItemDrop(enemy, world);
                    DestroySystem.BodyDestroyList.add(enemy.body);
                    DestroySystem.EntityDestroyList.add(e);
                }

                if (enemy.moveRight)
                    if (enemy.body.getLinearVelocity().x <= enemy.maxSpeed)
                        enemy.body.applyForceToCenter(10, 0, true);
                    else
                        enemy.body.setLinearVelocity(enemy.body.getLinearVelocity().x, enemy.body.getLinearVelocity().y);

                else if (enemy.moveLeft)
                    if (enemy.body.getLinearVelocity().x >= -1* enemy.maxSpeed)
                        enemy.body.applyForceToCenter(-10, 0, true);
                    else
                        enemy.body.setLinearVelocity(enemy.body.getLinearVelocity().x, enemy.body.getLinearVelocity().y);
                else
                    enemy.body.setLinearVelocity((float) (enemy.body.getLinearVelocity().x/1.5), enemy.body.getLinearVelocity().y);
                if (enemy.PlayerSpotted) {
                    timer++;
                    if (timer >= 80) {
                        timer = 0;
                        e = players.first();
                        PlayerComponent player = pm.get(e);
                        Shoot(world.world, enemy.sprite.getX(), enemy.sprite.getY(), player.sprite.getX() + player.sprite.getWidth() / 2, player.sprite.getY() + player.sprite.getHeight() / 2, enemy.sprite.isFlipX(), enemy.sprite.getWidth(), enemy.sprite.getHeight(), enemy.body, enemy.enemyID);
                    }
                }

                if (enemy.body.getLinearVelocity().x < 0 || enemy.body.getLinearVelocity().x > 0)
                    Animate(enemy);
            }
        }
    }

    private void Animate(EnemyComponent enemy)
    {
        if(enemy.body.getLinearVelocity().x < 0 && enemy.moveLeft && !enemy.sprite.isFlipX())
        {
            enemy.sprite.flip(true, false);
        }
        else if(enemy.body.getLinearVelocity().x > 0 && enemy.moveRight && enemy.sprite.isFlipX())
        {
            enemy.sprite.flip(true, false);
        }
    }

    private void Shoot(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double width, double height, Body enemyBody, int enemyID)
    {
        int weaponNmb = 0;
        switch (enemyID)
        {
            case 1:
                weaponNmb = 2; break;
            case 2:
                weaponNmb = 4; break;
            case 3:
                weaponNmb = 5; break;
        }

        switch (weaponNmb)
        {
            case 1: {
                weaponManager.createBall(world, charX, charY, pointerX, pointerY, flipX, width, height, enemyBody, weaponNmb, "Enemy");
                break;
            }
            case 2: {
                weaponManager.createBork(world, charX, charY, pointerX, pointerY, flipX, width, height, enemyBody, weaponNmb, "Enemy");
                break;
            }
            case 3: {
                weaponManager.createMissile(world, charX, charY, pointerX, pointerY, flipX, width, height, enemyBody, weaponNmb, "Enemy");
                break;
            }
            case 4: {
                weaponManager.createSponge(world, charX, charY, pointerX, pointerY, flipX, width, height, enemyBody, weaponNmb, "Enemy");
                break;
            }
            case 5: {
                weaponManager.createNet(world, charX, charY, pointerX, pointerY, flipX, width, height, enemyBody, weaponNmb, "Enemy");
                break;
            }
        }
    }

    private void ItemDrop(EnemyComponent enemy, WorldComponent world)
    {
        dropChance = MathUtils.random(1,2);
        if(dropChance == 1)
        {
            dropType = MathUtils.random(1,5);
            switch(dropType)
            {
                case 1:case 2:case 3:
                {
                    itemManager.createItem(dropType, enemy.body.getPosition().x, enemy.body.getPosition().y, world.world);
                    break;
                }
                case 4:
                case 5:
                {
                    for (int u = 0; u < MathUtils.random(20, 50); u++) {
                        itemManager.createItem(dropType, enemy.body.getPosition().x, enemy.body.getPosition().y, world.world);
                    }
                    break;
                }
            }
        }
    }
}
