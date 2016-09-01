package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
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
    public Sprite weaponSprite = new Sprite();
    private Sound dropSound;
    public Body weaponBody;
    public boolean destroy = false;
    private float bulletAngle;
    private Fixture weaponFixture;
    private int damageMade;
    Timer timer = new Timer();
    PhysicsComponent physicsComponent = new PhysicsComponent();
    SpriteComponent spriteComponent = new SpriteComponent();
    PositionComponent positionComponent = new PositionComponent();
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

        weaponFixture = weaponBody.createFixture(fixtureDef);
        ballCircle.dispose();

        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);
        weaponBody.setLinearVelocity((float) (pointerX - weaponBody.getPosition().x) + dogeBody.getLinearVelocity().x, (float) (pointerY - weaponBody.getPosition().y)+ dogeBody.getLinearVelocity().y);

        /*if(weaponBody.getLinearVelocity().x > 20 && weaponBody.getLinearVelocity().x > 0)
            weaponBody.setLinearVelocity(20, weaponBody.getLinearVelocity().y);
        else if(weaponBody.getLinearVelocity().x < -20 && weaponBody.getLinearVelocity().x < 0)
            weaponBody.setLinearVelocity(-20, weaponBody.getLinearVelocity().y);*/
    }

    private void createMissile(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, Body dogeBody)
    {
        damageMade = 20;
        //Loading the images
        weaponImage = new Texture("Rocket.png");

        weaponSprite = new Sprite(weaponImage);
        weaponSprite.setSize(weaponImage.getWidth()/5 * Game.WORLD_TO_BOX, weaponImage.getHeight()/5 * Game.WORLD_TO_BOX);
        weaponSprite.setOrigin(weaponSprite.getWidth() / 2, weaponSprite.getHeight() / 2);

        BodyDef weaponBodyDef = new BodyDef();
        weaponBodyDef.type = BodyDef.BodyType.DynamicBody;

        if(flipX == false) {
            weaponBodyDef.position.set(new Vector2(charX + (float) spriteWidth + (float) 0.4, charY + (float) 0.5));
        }
        else
            weaponBodyDef.position.set(new Vector2(charX - (float) 0.4, charY + (float) spriteHeight/2));

        weaponBody = world.createBody(weaponBodyDef);
        weaponBody.setGravityScale(0);

        PolygonShape weaponShape = new PolygonShape();
        weaponShape.setAsBox(weaponSprite.getWidth()/2, weaponSprite.getHeight());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = weaponShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit
        bulletAngle = (float) Math.atan2(pointerY - weaponBody.getPosition().y, pointerX - weaponBody.getPosition().x);
        weaponFixture = weaponBody.createFixture(fixtureDef);
        weaponShape.dispose();

        weaponBody.setLinearVelocity((float) (pointerX - weaponBody.getPosition().x + dogeBody.getLinearVelocity().x), (float) (pointerY - weaponBody.getPosition().y) + dogeBody.getLinearVelocity().y);
        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);
    }

    public void MissileExplode(World world, float missileX, float missileY, Body missileBody){
        Game.destroyList.add(missileBody);
        //Game.weaponDestroyList.add(missileBody);


        destroy = true;
    }
}
