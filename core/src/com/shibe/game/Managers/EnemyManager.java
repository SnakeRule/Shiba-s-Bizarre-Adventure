package com.shibe.game.Managers;

import com.badlogic.gdx.maps.MapObject;
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
    private int enemyType;
    private int spawnTrigger;
    private EnemyComponent enemyComponent;

    public EnemyManager(World world, float SpawnX, float SpawnY) {
        super(world, SpawnX, SpawnY);
        CreateDog(world, SpawnX, SpawnY);
    }

    private void CreateDog(World world, float SpawnX, float SpawnY)
    {
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

        PolygonShape RearSensorBox = new PolygonShape();
        RearSensorBox.setAsBox((float) 2.7,dogeSprite.getHeight(),new Vector2(-3,0),0);

        FixtureDef RearFixtureDef = new FixtureDef();
        RearFixtureDef.isSensor = true;
        RearFixtureDef.shape = RearSensorBox;
        RearFixtureDef.filter.categoryBits = CollisionFilterManager.ENEMY_SENSOR;
        RearFixtureDef.filter.maskBits = (short) (~CollisionFilterManager.NONE & CollisionFilterManager.PLAYER);

        Fixture RearFixture = dogeBody.createFixture(RearFixtureDef);
        RearFixture.setUserData("Left");

        RearSensorBox.dispose();

        PolygonShape FrontsensorBox = new PolygonShape();
        FrontsensorBox.setAsBox((float) 2.7,dogeSprite.getHeight(),new Vector2(3,0),0);

        FixtureDef FrontFixtureDef = new FixtureDef();
        FrontFixtureDef.isSensor = true;
        FrontFixtureDef.shape = FrontsensorBox;
        FrontFixtureDef.filter.categoryBits = CollisionFilterManager.ENEMY_SENSOR;
        FrontFixtureDef.filter.maskBits = (short) (~CollisionFilterManager.NONE & CollisionFilterManager.PLAYER);

        Fixture Frontfixture = dogeBody.createFixture(FrontFixtureDef);
        Frontfixture.setUserData("Right");

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilterManager.ENEMY;
        fixture.setFilterData(filter);
        fixture.setUserData("Enemy");
        dogeBody.setUserData(e);

        enemyComponent = new EnemyComponent();
        enemyComponent.setEnemy(Left,Right, Jump, dogeBody, feetFixture, dogeSprite, weapons, RearFixture, Frontfixture, canJump, health, spawnTrigger);
        e.add(enemyComponent);

        Game.engine.addEntity(e);
    }
}
