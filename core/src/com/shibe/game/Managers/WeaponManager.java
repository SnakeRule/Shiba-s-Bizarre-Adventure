package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.PositionComponent;
import com.shibe.game.Components.SpriteComponent;
import com.shibe.game.Components.WeaponComponent;

/**
 * Created by Jere on 12.8.2016.
 */
public class WeaponManager {

    private Texture ballImage = new Texture(Gdx.files.internal("Ball.png"));
    private Texture missileImage = new Texture("Rocket.png");
    private Texture borkImage = new Texture("Bork.png");
    private Texture spongeImage = new Texture("sponge.png");
    private Texture netImage = new Texture("net.png");
    private Sound barkSound = Gdx.audio.newSound(Gdx.files.internal("Bark_Sound.mp3"));
    private Sound dropSound;
    public Body weaponBody;
    private boolean destroy = false;
    private float bulletAngle;
    private Fixture weaponFixture;
    private int damageMade;
    private Vector2 bulletVector;
    private Vector2 bulletVectorNormalized;
    public Entity e;
    private Sprite weaponSprite;


    public void createBall(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, Body dogeBody, int weaponNmb, String owner)
    {
        damageMade = 0;
        //Loading the images
        //weaponImage = new Texture("Ball.png");

        weaponSprite = new Sprite(ballImage);
        weaponSprite.setSize((ballImage.getWidth() / 2 * Game.WORLD_TO_BOX),(ballImage.getHeight() / 2 * Game.WORLD_TO_BOX));
        weaponSprite.setOrigin(weaponSprite.getWidth() / 2, weaponSprite.getHeight() / 2);

        BodyDef dropBodyDef = new BodyDef();
        dropBodyDef.type = BodyDef.BodyType.DynamicBody;

        if(flipX == false)
            dropBodyDef.position.set(new Vector2(charX + (float)spriteWidth + weaponSprite.getWidth(), charY + (float) 0.5));
        else
            dropBodyDef.position.set(new Vector2(charX - weaponSprite.getWidth(), charY + (float) spriteHeight/2));

        weaponBody = world.createBody(dropBodyDef);


        CircleShape ballCircle = new CircleShape();
        ballCircle.setRadius(weaponSprite.getWidth()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = ballCircle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit
        bulletAngle = (float) Math.atan2(pointerY - charY, pointerX - charX);
        bulletVector = new Vector2((float) (pointerX - weaponBody.getPosition().x) + dogeBody.getLinearVelocity().x, (float) (pointerY - weaponBody.getPosition().y));
        bulletVectorNormalized = bulletVector.nor();

        weaponFixture = weaponBody.createFixture(fixtureDef);
        ballCircle.dispose();

        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);
        weaponBody.setLinearVelocity(bulletVectorNormalized.x * 6, bulletVectorNormalized.y * 6);

        addComponents(weaponNmb, owner);
    }

    public void createMissile(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, Body dogeBody, int weaponNmb, String owner)
    {
        damageMade = 30;

        weaponSprite = new Sprite(missileImage);
        weaponSprite.setSize(missileImage.getWidth()/8 * Game.WORLD_TO_BOX, missileImage.getHeight()/8 * Game.WORLD_TO_BOX);
        weaponSprite.setOrigin(weaponSprite.getWidth() / 2, weaponSprite.getHeight() / 2);

        BodyDef weaponBodyDef = new BodyDef();
        weaponBodyDef.type = BodyDef.BodyType.DynamicBody;

        if(flipX == false) {
            weaponBodyDef.position.set(new Vector2(charX + (float) spriteWidth + (float) 0.1, charY + (float) 0.4));
        }
        else
            weaponBodyDef.position.set(new Vector2(charX - (float) 0.1, charY + (float) + 0.4));

        weaponBody = world.createBody(weaponBodyDef);
        weaponBody.setGravityScale(0);

        PolygonShape weaponShape = new PolygonShape();
        weaponShape.setAsBox(weaponSprite.getWidth()/2, weaponSprite.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = weaponShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f; // Make it bounce a little bit
        bulletAngle = (float) Math.atan2(pointerY - weaponBody.getPosition().y, pointerX - weaponBody.getPosition().x);
        bulletVector = new Vector2((float) (pointerX - weaponBody.getPosition().x), (float) (pointerY - weaponBody.getPosition().y));
        bulletVectorNormalized = bulletVector.nor();
        weaponFixture = weaponBody.createFixture(fixtureDef);
        weaponShape.dispose();

        weaponBody.setLinearVelocity((bulletVectorNormalized.x * 7), bulletVectorNormalized.y * 7);
        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);
        addComponents(weaponNmb, owner);
    }

    public void createBork(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, Body dogeBody, int weaponNmb, String owner)
    {
        damageMade = 15;

        weaponSprite = new Sprite(borkImage);
        weaponSprite.setSize(borkImage.getWidth() * Game.WORLD_TO_BOX / 6, borkImage.getHeight() * Game.WORLD_TO_BOX / 6);
        weaponSprite.setOrigin(weaponSprite.getWidth() / 2, weaponSprite.getHeight() / 2);

        BodyDef weaponBodyDef = new BodyDef();
        weaponBodyDef.type = BodyDef.BodyType.DynamicBody;

        if(flipX == false) {
            weaponBodyDef.position.set(new Vector2(charX + (float) spriteWidth + (float) 0.1, charY + (float) 0.4));
        }
        else
            weaponBodyDef.position.set(new Vector2(charX - (float) 0.1, charY + (float) + 0.4));

        weaponBody = world.createBody(weaponBodyDef);
        weaponBody.setGravityScale(0);

        PolygonShape weaponShape = new PolygonShape();
        weaponShape.setAsBox(weaponSprite.getWidth()/2, (float) (weaponSprite.getHeight() /2));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = weaponShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f; // Make it bounce a little bit
        bulletAngle = (float) Math.atan2(pointerY - weaponBody.getPosition().y, pointerX - weaponBody.getPosition().x);
        bulletVector = new Vector2((float) (pointerX - weaponBody.getPosition().x), (float) (pointerY - weaponBody.getPosition().y));
        bulletVectorNormalized = bulletVector.nor();
        weaponFixture = weaponBody.createFixture(fixtureDef);
        weaponShape.dispose();

        weaponBody.setLinearVelocity((bulletVectorNormalized.x * 7), bulletVectorNormalized.y * 7);
        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);
        addComponents(weaponNmb, owner);

        barkSound.play(1.0f);
    }

    public void createSponge(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, Body dogeBody, int weaponNmb, String owner)
    {
        damageMade = 20;

        weaponSprite = new Sprite(spongeImage);
        weaponSprite.setSize((float) (spongeImage.getWidth()/1.3 * Game.WORLD_TO_BOX), (float) (spongeImage.getHeight()/1.3 * Game.WORLD_TO_BOX));
        weaponSprite.setOrigin(weaponSprite.getWidth() / 2, weaponSprite.getHeight() / 2);

        BodyDef weaponBodyDef = new BodyDef();
        weaponBodyDef.type = BodyDef.BodyType.DynamicBody;

        if(flipX == false) {
            weaponBodyDef.position.set(new Vector2(charX + (float) spriteWidth + (float) 0.1, charY + (float) 0.4));
        }
        else
            weaponBodyDef.position.set(new Vector2(charX - (float) 0.1, charY + (float) + 0.4));

        weaponBody = world.createBody(weaponBodyDef);
        weaponBody.setGravityScale((float) 0.5);

        PolygonShape weaponShape = new PolygonShape();
        weaponShape.setAsBox(weaponSprite.getWidth()/2, weaponSprite.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = weaponShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f; // Make it bounce a little bit
        bulletAngle = (float) Math.atan2(pointerY - weaponBody.getPosition().y, pointerX - weaponBody.getPosition().x);
        bulletVector = new Vector2((float) (pointerX - weaponBody.getPosition().x), (float) (pointerY - weaponBody.getPosition().y));
        bulletVectorNormalized = bulletVector.nor();
        weaponFixture = weaponBody.createFixture(fixtureDef);
        weaponShape.dispose();

        weaponBody.setLinearVelocity((bulletVectorNormalized.x * 7), bulletVectorNormalized.y * 7 + 2);
        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);
        addComponents(weaponNmb, owner);
    }

    public void createNet(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, Body dogeBody, int weaponNmb, String owner)
    {
        damageMade = 20;

        weaponSprite = new Sprite(netImage);
        weaponSprite.setSize((float) (spongeImage.getWidth()/1 * Game.WORLD_TO_BOX), (float) (spongeImage.getHeight()/1 * Game.WORLD_TO_BOX));
        weaponSprite.setOrigin(weaponSprite.getWidth() / 2, weaponSprite.getHeight() / 2);

        BodyDef weaponBodyDef = new BodyDef();
        weaponBodyDef.type = BodyDef.BodyType.DynamicBody;

        if(flipX == false) {
            weaponBodyDef.position.set(new Vector2(charX + (float) spriteWidth + (float) 0.1, charY + (float) 0.4));
        }
        else
            weaponBodyDef.position.set(new Vector2(charX - (float) 0.1, charY + (float) + 0.4));

        weaponBody = world.createBody(weaponBodyDef);
        weaponBody.setGravityScale((float) 0.5);

        PolygonShape weaponShape = new PolygonShape();
        weaponShape.setAsBox(weaponSprite.getWidth()/2, weaponSprite.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = weaponShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f; // Make it bounce a little bit
        bulletAngle = (float) Math.atan2(pointerY - weaponBody.getPosition().y, pointerX - weaponBody.getPosition().x);
        bulletVector = new Vector2((float) (pointerX - weaponBody.getPosition().x), (float) (pointerY - weaponBody.getPosition().y));
        bulletVectorNormalized = bulletVector.nor();
        weaponFixture = weaponBody.createFixture(fixtureDef);
        weaponShape.dispose();

        weaponBody.setLinearVelocity((bulletVectorNormalized.x * 7), bulletVectorNormalized.y * 7 + 2);
        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);
        addComponents(weaponNmb, owner);
    }

    private void addComponents(int weaponNmb, String Owner)
    {
        e = new Entity();
        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.setSprite(weaponSprite);
        e.add(spriteComponent);
        PositionComponent positionComponent = new PositionComponent();
        positionComponent.setX(weaponSprite.getX());
        positionComponent.setY(weaponSprite.getY());
        positionComponent.setAngle(weaponBody.getAngle());
        e.add(positionComponent);
        Filter filter = new Filter();
        if(weaponNmb == 1)
        {
            filter.categoryBits = CollisionFilterManager.COLLIDE_PROJECTILE;
            filter.maskBits = (short) (~CollisionFilterManager.NON_COLLIDE_PROJECTILE);
            weaponFixture.setFilterData(filter);
        }
        if(weaponNmb != 1 && Owner == "Player") {
            filter.categoryBits = CollisionFilterManager.NON_COLLIDE_PROJECTILE;
            filter.maskBits = (short) (~CollisionFilterManager.PLAYER & ~CollisionFilterManager.NON_COLLIDE_PROJECTILE);
            weaponFixture.setFilterData(filter);
        }
        else if(weaponNmb != 1 && Owner == "Enemy") {
            filter.categoryBits = CollisionFilterManager.NON_COLLIDE_PROJECTILE;
            filter.maskBits = (short) (~CollisionFilterManager.ENEMY & ~CollisionFilterManager.NON_COLLIDE_PROJECTILE);
            weaponFixture.setFilterData(filter);
        }
        weaponFixture.setUserData("Weapon");
        weaponBody.setUserData(e);
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBody(weaponBody);
        e.add(physicsComponent);
        WeaponComponent weaponComponent = new WeaponComponent();
        weaponComponent.setType(weaponNmb);
        weaponComponent.setDamage(damageMade);
        weaponComponent.setBody(weaponBody);
        weaponComponent.setOwner(Owner);
        e.add(weaponComponent);
        Game.engine.addEntity(e);
    }
}
