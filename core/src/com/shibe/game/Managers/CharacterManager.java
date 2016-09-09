package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.PositionComponent;
import com.shibe.game.Components.SpriteComponent;

import java.util.ArrayList;

/**
 * Created by Jere on 11.8.2016.
 */
class CharacterManager
{
    PositionComponent positionComponent = new PositionComponent();
    PhysicsComponent physicsComponent = new PhysicsComponent();
    SpriteComponent spriteComponent = new SpriteComponent();
    public Body DogeFeet;
    private BodyDef dogeFeetDef;
    Texture dogeImage1;
    Texture dogeImage2;
    Texture dogeImage3;
    public boolean TouchingLadder = false;
    Sprite dogeSprite;
    Body dogeBody;
    public WeaponManager weapon;
    public int footContacts = 0;
    protected int animationCounter;
    protected int fileno = 1;
    boolean moveLeft;
    boolean moveRight;
    boolean Jump;
    Fixture feetFixture;
    Fixture fixture;
    boolean canJump;
    Entity e = new Entity();


    CharacterManager(World world, float SpawnX, float SpawnY)
    {
        dogeImage1 = new Texture("SHIBA1.png");
        dogeImage2 = new Texture("SHIBA2.png");
        dogeImage3 = new Texture("SHIBA3.png");
        dogeSprite = new Sprite(dogeImage1);
        float size = (float) 1.6;
        dogeSprite.setSize((dogeImage1.getWidth()/ size)* Game.WORLD_TO_BOX, (dogeImage1.getHeight()/ size)* Game.WORLD_TO_BOX);

        //Create Body
        BodyDef dogeBodyDef = new BodyDef();
        dogeBodyDef.type = BodyDef.BodyType.DynamicBody;
        dogeBodyDef.fixedRotation = true;
        // set its position

        dogeBodyDef.position.set(new Vector2(SpawnX* Game.WORLD_TO_BOX, SpawnY* Game.WORLD_TO_BOX));

        dogeBody = world.createBody(dogeBodyDef);

        //Create a Polygon shape for the body
        PolygonShape dogeBox = new PolygonShape();
        dogeBox.setAsBox(dogeSprite.getWidth()/2, dogeSprite.getHeight()/2 - (float)0.01);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dogeBox;
        fixtureDef.density = (float) 1.5;
        fixtureDef.friction = (float) 0;

        //Create fixture
        fixture = dogeBody.createFixture(fixtureDef);
        // Clean up
        dogeBox.dispose();

        PolygonShape dogeFeetBox = new PolygonShape();
        dogeFeetBox.setAsBox((float) (dogeSprite.getWidth() - 0.43), (float) 0.05, new Vector2(0, (float) -0.33), 0);

        FixtureDef feetFixtureDef = new FixtureDef();
        feetFixtureDef.isSensor = true;
        feetFixtureDef.friction = (float) 1;
        feetFixtureDef.shape = dogeFeetBox;
        feetFixtureDef.filter.categoryBits = CollisionFilterManager.CHARACTER_FEET;
        feetFixtureDef.filter.maskBits = (short) (0x0000f & ~CollisionFilterManager.NON_COLLIDE_PROJECTILE & ~CollisionFilterManager.ENEMY_SENSOR);

        feetFixture = dogeBody.createFixture(feetFixtureDef);
        feetFixture.setUserData("Feet");
    }
}
