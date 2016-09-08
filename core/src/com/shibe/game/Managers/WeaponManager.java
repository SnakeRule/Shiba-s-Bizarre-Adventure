package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
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
    private WeaponComponent weaponComponent = new WeaponComponent();
    private Texture weaponImage;
    private Sprite weaponSprite = new Sprite();
    private Sound dropSound;
    public Body weaponBody;
    private boolean destroy = false;
    private float bulletAngle;
    private Fixture weaponFixture;
    private int damageMade;
    private PhysicsComponent physicsComponent = new PhysicsComponent();
    private SpriteComponent spriteComponent = new SpriteComponent();
    private PositionComponent positionComponent = new PositionComponent();
    private Vector2 bulletVector;
    private Vector2 bulletVectorNormalized;
    public Entity e = new Entity();

    public WeaponManager(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, int weaponNmb, Body dogeBody, String Owner)
    {
        switch (weaponNmb)
        {
            case 1:{
                createBall(world,charX,charY,pointerX,pointerY,flipX,spriteWidth,spriteHeight, dogeBody);
                break;
            }
            case 2:{
                createMissile(world,charX,charY,pointerX,pointerY,flipX,spriteWidth,spriteHeight, dogeBody);
                break;
            }
        }
        spriteComponent.setSprite(weaponSprite);
        e.add(spriteComponent);
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
        physicsComponent.setBody(weaponBody);
        e.add(physicsComponent);
        weaponComponent.setType(weaponNmb);
        weaponComponent.setEntity(e);
        weaponComponent.setDamage(damageMade);
        weaponComponent.setBody(weaponBody);
        weaponComponent.setOwner(Owner);
        e.add(weaponComponent);
        Game.engine.addEntity(e);
    }


    private void createBall(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, Body dogeBody)
    {
        damageMade = 0;
        //Loading the images
        weaponImage = new Texture("Ball.png");
        
        weaponSprite = new Sprite(weaponImage);
        weaponSprite.setSize(weaponImage.getWidth()/2 * Game.WORLD_TO_BOX, weaponImage.getHeight()/2 * Game.WORLD_TO_BOX);
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
        fixtureDef.density = 0.8f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit
        bulletAngle = (float) Math.atan2(pointerY - charY, pointerX - charX);
        bulletVector = new Vector2((float) (pointerX - weaponBody.getPosition().x) + dogeBody.getLinearVelocity().x, (float) (pointerY - weaponBody.getPosition().y));
        bulletVectorNormalized = bulletVector.nor();

        weaponFixture = weaponBody.createFixture(fixtureDef);
        ballCircle.dispose();

        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);
        weaponBody.setLinearVelocity(bulletVectorNormalized.x * 6, bulletVectorNormalized.y * 6);
    }

    private void createMissile(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, Body dogeBody)
    {
        damageMade = 20;
        //Loading the images
        weaponImage = new Texture("Rocket.png");

        weaponSprite = new Sprite(weaponImage);
        weaponSprite.setSize(weaponImage.getWidth()/8 * Game.WORLD_TO_BOX, weaponImage.getHeight()/8 * Game.WORLD_TO_BOX);
        weaponSprite.setOrigin(weaponSprite.getWidth() / 2, weaponSprite.getHeight() / 2);

        BodyDef weaponBodyDef = new BodyDef();
        weaponBodyDef.type = BodyDef.BodyType.DynamicBody;

        if(flipX == false) {
            weaponBodyDef.position.set(new Vector2(charX + (float) spriteWidth + (float) 0.1, charY + (float) 0.5));
        }
        else
            weaponBodyDef.position.set(new Vector2(charX - (float) 0.1, charY + (float) spriteHeight/2));

        weaponBody = world.createBody(weaponBodyDef);
        weaponBody.setGravityScale(0);

        PolygonShape weaponShape = new PolygonShape();
        weaponShape.setAsBox(weaponSprite.getWidth()/2, weaponSprite.getHeight());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = weaponShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f; // Make it bounce a little bit
        bulletAngle = (float) Math.atan2(pointerY - weaponBody.getPosition().y, pointerX - weaponBody.getPosition().x);
        bulletVector = new Vector2((float) (pointerX - weaponBody.getPosition().x) + dogeBody.getLinearVelocity().x, (float) (pointerY - weaponBody.getPosition().y));
        bulletVectorNormalized = bulletVector.nor();
        weaponFixture = weaponBody.createFixture(fixtureDef);
        weaponShape.dispose();

        weaponBody.setLinearVelocity((bulletVectorNormalized.x * 7), bulletVectorNormalized.y * 7);
        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);
    }
}
