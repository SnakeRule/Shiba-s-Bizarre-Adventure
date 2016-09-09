package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.shibe.game.Components.EnemyComponent;
import com.shibe.game.Components.PlayerComponent;
import com.shibe.game.Components.WorldComponent;
import com.shibe.game.Managers.WeaponManager;

/**
 * Created by Jere on 30.8.2016.
 */
public class EnemySystem extends EntitySystem
{
    private int timer = 20;
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
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        for(int i = 0; i < enemies.size(); i++) {
            Entity e = enemies.get(i);
            EnemyComponent enemy = em.get(e);

                if (enemy.health <= 0) {
                    DestroySystem.BodyDestroyList.add(enemy.body);
                    DestroySystem.EntityDestroyList.add(e);
                }

                if (enemy.moveLeft) {
                    if (enemy.body.getLinearVelocity().x >= -3)
                        enemy.body.applyForceToCenter(-30, 0, true);
                } else if (enemy.moveRight) {
                    if (enemy.body.getLinearVelocity().x <= 3)
                        enemy.body.applyForceToCenter(30, 0, true);
                } else {
                    enemy.body.setLinearVelocity(0, enemy.body.getLinearVelocity().y);
                }
            if(enemy.PlayerSpotted)
            {
                timer++;
                if(timer >= 20) {
                    timer = 0;
                    e = worlds.first();
                    WorldComponent world = wm.get(e);
                    e = players.first();
                    PlayerComponent player = pm.get(e);
                    Shoot(world.world, enemy.sprite.getX(), enemy.sprite.getY(), player.sprite.getX() + player.sprite.getWidth() / 2, player.sprite.getY() + player.sprite.getHeight() / 2, enemy.sprite.isFlipX(), enemy.sprite.getWidth(), enemy.sprite.getHeight(), 2, enemy.body);
                }
            }

                if (enemy.body.getLinearVelocity().x < 0 || enemy.body.getLinearVelocity().x > 0)
                    Animate(enemy);
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

    private void Shoot(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double width, double height, int weaponNmb, Body dogeBody)
    {
        weapon = new WeaponManager(world, charX, charY, pointerX, pointerY, flipX, width, height, weaponNmb, dogeBody, "Enemy");
    }
}
