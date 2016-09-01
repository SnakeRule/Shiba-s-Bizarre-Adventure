package com.shibe.game.Managers;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.EnemyComponent;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.SpriteComponent;

/**
 * Created by Jere on 18.8.2016.
 */
public class EnemyManager extends CharacterManager
{
    private boolean Left;
    private boolean Right;
    private boolean Jump;
    private int health;
    private EnemyComponent enemyComponent;

    public EnemyManager(World world, RectangleMapObject rect, TiledMap map) {
        super(world, rect, map);

        health = 100;

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

        PolygonShape leftSensorBox = new PolygonShape();
        leftSensorBox.setAsBox((float) 2.7,dogeSprite.getHeight(),new Vector2(-3,0),0);

        FixtureDef leftFixtureDef = new FixtureDef();
        leftFixtureDef.isSensor = true;
        leftFixtureDef.shape = leftSensorBox;
        leftFixtureDef.filter.categoryBits = 0x0004;
        leftFixtureDef.filter.maskBits = 0x0008;

        Fixture leftFixture = dogeBody.createFixture(leftFixtureDef);
        leftFixture.setUserData("Left");

        leftSensorBox.dispose();

        PolygonShape RightsensorBox = new PolygonShape();
        RightsensorBox.setAsBox((float) 2.7,dogeSprite.getHeight(),new Vector2(3,0),0);

        FixtureDef RightFixtureDef = new FixtureDef();
        RightFixtureDef.isSensor = true;
        RightFixtureDef.shape = RightsensorBox;
        RightFixtureDef.filter.categoryBits = 0x0004;
        RightFixtureDef.filter.maskBits = 0x0008;

        Fixture Rightfixture = dogeBody.createFixture(RightFixtureDef);
        Rightfixture.setUserData("Right");

        fixture.setUserData("EnemyManager");
        dogeBody.setUserData(e);

        enemyComponent = new EnemyComponent();
        enemyComponent.setEnemy(Left,Right, Jump, dogeBody, feetFixture, dogeSprite, weapons, leftFixture, Rightfixture, canJump, health);
        e.add(enemyComponent);

        Game.engine.addEntity(e);
    }
}
