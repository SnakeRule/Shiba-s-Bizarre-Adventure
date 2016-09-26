package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.PlayerComponent;
import com.shibe.game.Components.PositionComponent;
import com.shibe.game.Components.SpriteComponent;

/**
 * Created by Jere on 18.8.2016.
 */
class PlayerManager {
    PositionComponent positionComponent = new PositionComponent();
    PhysicsComponent physicsComponent = new PhysicsComponent();
    SpriteComponent spriteComponent = new SpriteComponent();
    public Body DogeFeet;
    private BodyDef dogeFeetDef;
    TextureRegion dogeRegion = new TextureRegion();
    Texture dogeImage = new Texture("SHIBA.png");
    public boolean TouchingLadder = false;
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

    public PlayerManager()
    {
        dogeRegion.setTexture(dogeImage);
        dogeRegion.setRegion(0,0,dogeImage.getWidth()/3,dogeImage.getHeight());
    }


    public void CreatePlayer(World world, Float SpawnX, float SpawnY) {
        Sprite dogeSprite = new Sprite(dogeRegion);
        float size = (float) 2.1;
        dogeSprite.setSize((dogeImage.getWidth()/3 / size)* Game.WORLD_TO_BOX, (dogeImage.getHeight()/ size)* Game.WORLD_TO_BOX);

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
        fixtureDef.friction = (float) 0.0;

        //Create fixture
        fixture = dogeBody.createFixture(fixtureDef);
        // Clean up
        dogeBox.dispose();

        PolygonShape dogeFeetBox = new PolygonShape();
        dogeFeetBox.setAsBox((float) (dogeSprite.getWidth() - 0.30), (float) 0.05, new Vector2(0, (float) -0.25), 0);

        FixtureDef feetFixtureDef = new FixtureDef();
        feetFixtureDef.isSensor = true;
        feetFixtureDef.friction = (float) 1;
        feetFixtureDef.shape = dogeFeetBox;
        feetFixtureDef.filter.categoryBits = CollisionFilterManager.CHARACTER_FEET;
        feetFixtureDef.filter.maskBits = (short) (CollisionFilterManager.NONE & ~CollisionFilterManager.NON_COLLIDE_PROJECTILE & ~CollisionFilterManager.ENEMY_SENSOR);

        feetFixture = dogeBody.createFixture(feetFixtureDef);
        feetFixture.setUserData("Feet");

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

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilterManager.PLAYER;
        fixture.setFilterData(filter);

        fixture.setUserData("Player");
        dogeBody.setUserData(e);

        PlayerComponent playerComponent = new PlayerComponent();
        playerComponent.setPlayer(moveLeft, moveRight, Jump, dogeBody, feetFixture, fixture, dogeSprite, canJump, dogeImage);
        e.add(playerComponent);

        Game.engine.addEntity(e);
    }
}
