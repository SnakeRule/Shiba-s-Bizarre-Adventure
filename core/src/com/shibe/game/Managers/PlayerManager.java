package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.PlayerComponent;
import com.shibe.game.Components.PositionComponent;
import com.shibe.game.Components.SpriteComponent;

/**
 * Created by Jere on 18.8.2016.
 */
class PlayerManager extends CharacterManager {
    public Sprite AimReticle;
    private Entity playerEntity = new Entity();
    private PlayerComponent playerComponent = new PlayerComponent();

    public PlayerManager(World world, RectangleMapObject rect, TiledMap map) {
        super(world, rect, map);

        positionComponent = new PositionComponent();
        positionComponent.setX(dogeBody.getPosition().x);
        positionComponent.setY(dogeBody.getPosition().y);
        positionComponent.setAngle(dogeBody.getAngle());
        e.add(positionComponent);

        spriteComponent = new SpriteComponent();
        spriteComponent.setSprite(dogeSprite);
        e.add(spriteComponent);

        physicsComponent = new PhysicsComponent();
        physicsComponent.setBody(dogeBody);
        e.add(physicsComponent);

        feetFixture.setUserData("PlayerFeet");
        fixture.setUserData("PlayerManager");
        dogeBody.setUserData(e);

        playerComponent.setPlayer(moveLeft,moveRight,Jump, dogeBody, feetFixture, fixture, dogeSprite, weapons, canJump, dogeImage1, dogeImage2, dogeImage3);
        e.add(playerComponent);

        Game.engine.addEntity(e);
    }
}
