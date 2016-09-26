package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.EnemyComponent;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.PositionComponent;
import com.shibe.game.Components.SpriteComponent;

/**
 * Created by Jere on 18.8.2016.
 */
public class EnemyManager
{
    PositionComponent positionComponent = new PositionComponent();
    PhysicsComponent physicsComponent = new PhysicsComponent();
    SpriteComponent spriteComponent = new SpriteComponent();
    public Body DogeFeet;
    int health;
    private BodyDef dogeFeetDef;
    public boolean TouchingLadder = false;
    Body enemyBody;
    Sprite enemySprite;
    public WeaponManager weapon;
    public int footContacts = 0;
    protected int animationCounter;
    protected int fileno = 1;
    boolean moveLeft;
    boolean moveRight;
    Fixture feetFixture;
    Fixture fixture;
    boolean canJump;
    Entity e;
    private boolean Left;
    private boolean Right;
    private boolean Jump;
    private float maxSpeed;

    TextureRegion dogeRegion = new TextureRegion();
    Texture dogeImage = new Texture("SHIBA.png");
    TextureRegion tubRegion = new TextureRegion();
    Texture tubImage = new Texture("tub.png");
    TextureRegion hat_manRegion = new TextureRegion();
    Texture hat_manImage = new Texture("hat_man.png");

    public EnemyManager()
    {
        dogeRegion.setTexture(dogeImage);
        dogeRegion.setRegion(0,0, dogeImage.getWidth()/3, dogeImage.getHeight());
        tubRegion.setTexture(tubImage);
        tubRegion.setRegion(0,0, tubImage.getWidth(), tubImage.getHeight());
        hat_manRegion.setTexture(hat_manImage);
        hat_manRegion.setRegion(0,0, hat_manImage.getWidth()/4, hat_manImage.getHeight());
    }

    public void createEnemy(World world, float SpawnX, float SpawnY, int enemyType)
    {
        e = new Entity();

        switch (enemyType)
        {
            case 1:
            {
                CreateDog(world, SpawnX, SpawnY);
                break;
            }
            case 2:
            {
                CreateTub(world, SpawnX, SpawnY);
                break;
            }
            case 3:
            {
                CreateHatMan(world, SpawnX, SpawnY);
                break;
            }
        }

        positionComponent.setX(enemyBody.getPosition().x);
        positionComponent.setY(enemyBody.getPosition().y);
        positionComponent.setAngle(enemyBody.getAngle());
        e.add(positionComponent);

        spriteComponent = new SpriteComponent();
        spriteComponent.setSprite(enemySprite);
        e.add(spriteComponent);

        physicsComponent = new PhysicsComponent();
        physicsComponent.setBody(enemyBody);
        e.add(physicsComponent);

        PolygonShape RearSensorBox = new PolygonShape();
        RearSensorBox.setAsBox((float) 2.7,enemySprite.getHeight(),new Vector2(-3,0),0);

        FixtureDef RearFixtureDef = new FixtureDef();
        RearFixtureDef.isSensor = true;
        RearFixtureDef.shape = RearSensorBox;
        RearFixtureDef.filter.categoryBits = CollisionFilterManager.ENEMY_SENSOR;
        RearFixtureDef.filter.maskBits = (short) (~CollisionFilterManager.NONE & CollisionFilterManager.PLAYER);

        Fixture RearFixture = enemyBody.createFixture(RearFixtureDef);
        RearFixture.setUserData("Left");

        RearSensorBox.dispose();

        PolygonShape FrontsensorBox = new PolygonShape();
        FrontsensorBox.setAsBox((float) 2.7,enemySprite.getHeight(),new Vector2(3,0),0);

        FixtureDef FrontFixtureDef = new FixtureDef();
        FrontFixtureDef.isSensor = true;
        FrontFixtureDef.shape = FrontsensorBox;
        FrontFixtureDef.filter.categoryBits = CollisionFilterManager.ENEMY_SENSOR;
        FrontFixtureDef.filter.maskBits = (short) (~CollisionFilterManager.NONE & CollisionFilterManager.PLAYER);

        Fixture Frontfixture = enemyBody.createFixture(FrontFixtureDef);
        Frontfixture.setUserData("Right");

        Filter filter = new Filter();
        filter.categoryBits = CollisionFilterManager.ENEMY;
        fixture.setFilterData(filter);
        fixture.setUserData("Enemy");
        enemyBody.setUserData(e);

        EnemyComponent enemyComponent = new EnemyComponent();
        enemyComponent.setEnemy(Left,Right, Jump, enemyBody, feetFixture, enemySprite, RearFixture, Frontfixture, canJump, health, enemyType, maxSpeed);
        e.add(enemyComponent);

        Game.engine.addEntity(e);
    }

    public void CreateDog(World world, float SpawnX, float SpawnY)
    {
        enemySprite = new Sprite(dogeRegion);
        float size = (float) 2.1;
        enemySprite.setSize((dogeImage.getWidth() / 3 / size)* Game.WORLD_TO_BOX, (dogeImage.getHeight()/ size)* Game.WORLD_TO_BOX);

        //Create Body
        BodyDef enemyBodyDef = new BodyDef();
        enemyBodyDef.type = BodyDef.BodyType.DynamicBody;
        enemyBodyDef.fixedRotation = true;
        // set its position

        enemyBodyDef.position.set(new Vector2(SpawnX* Game.WORLD_TO_BOX, SpawnY* Game.WORLD_TO_BOX));

        enemyBody = world.createBody(enemyBodyDef);

        //Create a Polygon shape for the body
        PolygonShape enemyBox = new PolygonShape();
        enemyBox.setAsBox(enemySprite.getWidth()/2, enemySprite.getHeight()/2 - (float)0.01);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = enemyBox;
        fixtureDef.density = (float) 1.5;
        fixtureDef.friction = (float) 0;

        //Create fixture
        fixture = enemyBody.createFixture(fixtureDef);
        // Clean up
        enemyBox.dispose();

        PolygonShape dogeFeetBox = new PolygonShape();
        dogeFeetBox.setAsBox((float) (enemySprite.getWidth() - 0.30), (float) 0.05, new Vector2(0, (float) -0.25), 0);

        FixtureDef feetFixtureDef = new FixtureDef();
        feetFixtureDef.isSensor = true;
        feetFixtureDef.friction = (float) 1;
        feetFixtureDef.shape = dogeFeetBox;
        feetFixtureDef.filter.categoryBits = CollisionFilterManager.CHARACTER_FEET;
        feetFixtureDef.filter.maskBits = (short) (0x0000f & ~CollisionFilterManager.NON_COLLIDE_PROJECTILE & ~CollisionFilterManager.ENEMY_SENSOR);

        feetFixture = enemyBody.createFixture(feetFixtureDef);
        feetFixture.setUserData("Feet");

        maxSpeed = 3;
        health = 100;
    }

    public void CreateTub(World world, float SpawnX, float SpawnY)
    {
        enemySprite = new Sprite(tubRegion);
        float size = (float) 1;
        enemySprite.setSize((tubImage.getWidth() / size)* Game.WORLD_TO_BOX, (tubImage.getHeight()/ size)* Game.WORLD_TO_BOX);

        //Create Body
        BodyDef enemyBodyDef = new BodyDef();
        enemyBodyDef.type = BodyDef.BodyType.DynamicBody;
        enemyBodyDef.fixedRotation = true;
        // set its position

        enemyBodyDef.position.set(new Vector2(SpawnX* Game.WORLD_TO_BOX, SpawnY* Game.WORLD_TO_BOX));

        enemyBody = world.createBody(enemyBodyDef);

        //Create a Polygon shape for the body
        PolygonShape enemyBox = new PolygonShape();
        enemyBox.setAsBox(enemySprite.getWidth()/2, enemySprite.getHeight()/2 - (float)0.01);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = enemyBox;
        fixtureDef.density = (float) 1.5;
        fixtureDef.friction = (float) 0;

        //Create fixture
        fixture = enemyBody.createFixture(fixtureDef);
        // Clean up
        enemyBox.dispose();

        PolygonShape dogeFeetBox = new PolygonShape();
        dogeFeetBox.setAsBox((float) (enemySprite.getWidth() - 0.30), (float) 0.05, new Vector2(0, (float) -0.25), 0);

        FixtureDef feetFixtureDef = new FixtureDef();
        feetFixtureDef.isSensor = true;
        feetFixtureDef.friction = (float) 1;
        feetFixtureDef.shape = dogeFeetBox;
        feetFixtureDef.filter.categoryBits = CollisionFilterManager.CHARACTER_FEET;
        feetFixtureDef.filter.maskBits = (short) (0x0000f & ~CollisionFilterManager.NON_COLLIDE_PROJECTILE & ~CollisionFilterManager.ENEMY_SENSOR);

        feetFixture = enemyBody.createFixture(feetFixtureDef);
        feetFixture.setUserData("Feet");

        maxSpeed = 2;
        health = 200;
    }

    public void CreateHatMan(World world, float SpawnX, float SpawnY)
    {
        enemySprite = new Sprite(hat_manRegion);
        float size = (float) 1.5;
        enemySprite.setSize((hat_manImage.getWidth()/4 / size)* Game.WORLD_TO_BOX, (hat_manImage.getHeight() / size)* Game.WORLD_TO_BOX);

        //Create Body
        BodyDef enemyBodyDef = new BodyDef();
        enemyBodyDef.type = BodyDef.BodyType.DynamicBody;
        enemyBodyDef.fixedRotation = true;
        // set its position

        enemyBodyDef.position.set(new Vector2(SpawnX* Game.WORLD_TO_BOX, SpawnY* Game.WORLD_TO_BOX));

        enemyBody = world.createBody(enemyBodyDef);

        //Create a Polygon shape for the body
        PolygonShape enemyBox = new PolygonShape();
        enemyBox.setAsBox(enemySprite.getWidth()/2, enemySprite.getHeight()/2 - (float)0.01);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = enemyBox;
        fixtureDef.density = (float) 1.5;
        fixtureDef.friction = (float) 0;

        //Create fixture
        fixture = enemyBody.createFixture(fixtureDef);
        // Clean up
        enemyBox.dispose();

        PolygonShape enemyFeetBox = new PolygonShape();
        enemyFeetBox.setAsBox((float) (enemySprite.getWidth() - 0.30), (float) 0.05, new Vector2(0, (float) -0.25), 0);

        FixtureDef feetFixtureDef = new FixtureDef();
        feetFixtureDef.isSensor = true;
        feetFixtureDef.friction = (float) 1;
        feetFixtureDef.shape = enemyFeetBox;
        feetFixtureDef.filter.categoryBits = CollisionFilterManager.CHARACTER_FEET;
        feetFixtureDef.filter.maskBits = (short) (0x0000f & ~CollisionFilterManager.NON_COLLIDE_PROJECTILE & ~CollisionFilterManager.ENEMY_SENSOR);

        feetFixture = enemyBody.createFixture(feetFixtureDef);
        feetFixture.setUserData("Feet");

        maxSpeed = 3;
        health = 150;
    }
}
