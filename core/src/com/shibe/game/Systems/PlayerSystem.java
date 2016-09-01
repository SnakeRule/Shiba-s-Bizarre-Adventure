package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.shibe.game.Managers.ActionProcessor;
import com.shibe.game.Components.CursorComponent;
import com.shibe.game.Components.PlayerComponent;
import com.shibe.game.Components.WorldComponent;
import com.shibe.game.Managers.WeaponManager;

/**
 * Created by Jere on 26.8.2016.
 */
public class PlayerSystem extends EntitySystem
{
    private Body body;
    private PlayerComponent player;
    private WeaponManager weapon;
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> worlds;
    private ImmutableArray<Entity> cursors;
    private int ballTimer = 15;
    private int missileTimer = 25;
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
    private ComponentMapper<CursorComponent> cm = ComponentMapper.getFor(CursorComponent.class);
    private int animationCounter;
    private int fileno = 1;

    public PlayerSystem(){}

    @Override
    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
        worlds = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
        cursors = engine.getEntitiesFor(Family.all(CursorComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        Entity entity = players.get(0);
        PlayerComponent player = pm.get(entity);

        if(player.feetCollisions > 0)
            player.canJump = true;
        else
            player.canJump = false;

        if(player.TouchingLadder == true) {
            player.body.setGravityScale(0);
        }
        else
            player.body.setGravityScale(1);

        if(ActionProcessor.RIGHT_PRESSED)
            if(player.body.getLinearVelocity().x <= 5)
                player.body.applyForceToCenter(30,0,true);
            else
                player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y);

        else if(ActionProcessor.LEFT_PRESSED)
            if(player.body.getLinearVelocity().x >= -5)
                player.body.applyForceToCenter(-30,0,true);
            else
                player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y);
        else
            player.body.setLinearVelocity(0,player.body.getLinearVelocity().y);

        if(!player.TouchingLadder && ActionProcessor.JUMP && player.canJump)
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, 6);
        else if (player.TouchingLadder && ActionProcessor.JUMP)
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, 4);

        if(ActionProcessor.DOWN && player.TouchingLadder)
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, -4);

        if(!ActionProcessor.DOWN && !ActionProcessor.JUMP && player.TouchingLadder)
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, 0);


        if(ActionProcessor.SHOOT) {
            Entity worldEntity = worlds.get(0);
            WorldComponent world = wm.get(worldEntity);
            Entity cursorEntity = cursors.get(0);
            CursorComponent cursor = cm.get(cursorEntity);

            if(ActionProcessor.WeaponNmb == 1) {
                if(ballTimer >= 15) {
                    Shoot(world.world, player.sprite.getX(), player.sprite.getY(), (cursor.sprite.getX() + player.sprite.getWidth() / 2), cursor.sprite.getY() + cursor.sprite.getHeight() / 2, player.sprite.isFlipX(), player.sprite.getWidth(), player.sprite.getHeight(), ActionProcessor.WeaponNmb, player.body, player);
                    ballTimer = 0;
                }
                ballTimer++;
            }
            if(ActionProcessor.WeaponNmb == 2)
            {
                if(missileTimer >= 25)
                {
                    Shoot(world.world, player.sprite.getX(), player.sprite.getY(), (cursor.sprite.getX() + player.sprite.getWidth() / 2), cursor.sprite.getY() + cursor.sprite.getHeight() / 2, player.sprite.isFlipX(), player.sprite.getWidth(), player.sprite.getHeight(), ActionProcessor.WeaponNmb, player.body, player);
                    missileTimer = 0;
                }
                missileTimer++;
            }
            if(ActionProcessor.WeaponNmb == 3) {
                if(ballTimer >= 1) {
                    Shoot(world.world, player.sprite.getX(), player.sprite.getY(), (cursor.sprite.getX() + player.sprite.getWidth() / 2), cursor.sprite.getY() + cursor.sprite.getHeight() / 2, player.sprite.isFlipX(), player.sprite.getWidth(), player.sprite.getHeight(), 1, player.body, player);
                    ballTimer = 0;
                }
                ballTimer++;
            }
        }
        if(!ActionProcessor.SHOOT)
        {
            ballTimer = 15;
            missileTimer = 25;
        }

        if(player.body.getLinearVelocity().x < 0 || player.body.getLinearVelocity().x > 0)
            Animate();

            super.update(deltaTime);
    }

    private void Shoot(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double width, double height, int weaponNmb, Body dogeBody, PlayerComponent player)
    {
        weapon = new WeaponManager(world, charX, charY, pointerX, pointerY, flipX, width, height, weaponNmb, dogeBody, "PlayerManager");
    }

    private void Animate()
    {
        Entity entity = players.get(0);
        PlayerComponent player = pm.get(entity);

        animationCounter++;

        if(animationCounter >= 8 && (ActionProcessor.LEFT_PRESSED || ActionProcessor.RIGHT_PRESSED))
        {
            switch (fileno)
            {
                case 1:
                    player.sprite.setTexture(player.texture1);
                    break;
                case 2:
                    player.sprite.setTexture(player.texture2);
                    break;
                case 3:
                    player.sprite.setTexture(player.texture3);
                    break;
            }
            if(fileno < 3)
                fileno++;
            else
                fileno = 1;
            animationCounter = 0;
        }
        if(player.body.getLinearVelocity().x > 0 && player.sprite.isFlipX() == true && ActionProcessor.RIGHT_PRESSED == true)
        {
            player.sprite.flip(true,false);
        }
        if(player.body.getLinearVelocity().x < 0 && player.sprite.isFlipX() == false && ActionProcessor.LEFT_PRESSED == true)
        {
            player.sprite.flip(true,false);
        }
    }
}
