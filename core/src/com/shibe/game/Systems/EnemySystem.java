package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.shibe.game.Components.EnemyComponent;

/**
 * Created by Jere on 30.8.2016.
 */
public class EnemySystem extends EntitySystem
{
    private ComponentMapper<EnemyComponent> em = ComponentMapper.getFor(EnemyComponent.class);
    private ImmutableArray<Entity> enemies;

    public EnemySystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for(int i = 0; i < enemies.size(); i++)
        {
            Entity e = enemies.get(i);
            EnemyComponent enemy = em.get(e);

            if(enemy.health <= 0)
            {
                DestroySystem.BodyDestroyList.add(enemy.body);
                DestroySystem.EntityDestroyList.add(e);
            }

            if(enemy.moveLeft)
            {
                if(enemy.body.getLinearVelocity().x >= -3)
                    enemy.body.applyForceToCenter(-30,0,true);
            }
            else if(enemy.moveRight)
            {
                if(enemy.body.getLinearVelocity().x <= 3)
                    enemy.body.applyForceToCenter(30,0,true);
            }
            else
            {
                enemy.body.setLinearVelocity(0,enemy.body.getLinearVelocity().y);
            }

            if(enemy.body.getLinearVelocity().x < 0 || enemy.body.getLinearVelocity().x > 0)
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
}
