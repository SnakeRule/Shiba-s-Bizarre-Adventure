package com.shibe.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Jere on 12.8.2016.
 */
public class Weapon {
    private Texture weaponImage;
    public Sprite weaponSprite = new Sprite();
    private Sound dropSound;
    public Body weaponBody;
    public boolean destroy = false;
    private float bulletAngle;
    Timer timer = new Timer();

    public Weapon(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, int weaponNmb, Body dogeBody)
    {
        switch (weaponNmb)
        {
            case 1:{
                createBullet(world,charX,charY,pointerX,pointerY,flipX,spriteWidth,spriteHeight, dogeBody);
                break;
            }
            case 2:{
                createRocket(world,charX,charY,pointerX,pointerY,flipX,spriteWidth,spriteHeight, dogeBody);
                break;
            }
        }
    }


    private void createBullet(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, Body dogeBody)
    {
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

        CircleShape dropBox = new CircleShape();
        dropBox.setRadius(weaponSprite.getWidth()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dropBox;
        fixtureDef.density = 0.3f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit
        bulletAngle = (float) Math.atan2(pointerY - charY, pointerX - charX);

        weaponBody.createFixture(fixtureDef);
        dropBox.dispose();
        weaponBody.setUserData(weaponSprite);

        weaponBody.setLinearVelocity((float) (pointerX - weaponBody.getPosition().x) + dogeBody.getLinearVelocity().x, (float) (pointerY - weaponBody.getPosition().y)+ dogeBody.getLinearVelocity().y);
        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);

        /*if(weaponBody.getLinearVelocity().x > 20 && weaponBody.getLinearVelocity().x > 0)
            weaponBody.setLinearVelocity(20, weaponBody.getLinearVelocity().y);
        else if(weaponBody.getLinearVelocity().x < -20 && weaponBody.getLinearVelocity().x < 0)
            weaponBody.setLinearVelocity(-20, weaponBody.getLinearVelocity().y);*/
    }

    private void createRocket(World world, float charX, float charY, double pointerX, double pointerY, boolean flipX, double spriteWidth, double spriteHeight, Body dogeBody)
    {
        //Loading the images
        weaponImage = new Texture("Rocket.png");

        weaponSprite = new Sprite(weaponImage);
        weaponSprite.setSize(weaponImage.getWidth()/5 * Game.WORLD_TO_BOX, weaponImage.getHeight()/5 * Game.WORLD_TO_BOX);
        weaponSprite.setOrigin(weaponSprite.getWidth() / 2, weaponSprite.getHeight() / 2);

        BodyDef weaponBodyDef = new BodyDef();
        weaponBodyDef.type = BodyDef.BodyType.KinematicBody;

        if(flipX == false) {
            weaponBodyDef.position.set(new Vector2(charX + (float) spriteWidth + (float) 0.4, charY + (float) 0.5));
        }
        else
            weaponBodyDef.position.set(new Vector2(charX - (float) 0.4, charY + (float) spriteHeight/2));

        weaponBody = world.createBody(weaponBodyDef);

        PolygonShape weaponShape = new PolygonShape();
        weaponShape.setAsBox(weaponSprite.getWidth()/2, weaponSprite.getHeight());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = weaponShape;
        fixtureDef.density = 0.3f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit
        bulletAngle = (float) Math.atan2(pointerY - weaponBody.getPosition().y, pointerX - weaponBody.getPosition().x);
        weaponBody.createFixture(fixtureDef);
        weaponShape.dispose();
        weaponBody.setUserData(weaponSprite);

        weaponBody.setLinearVelocity((float) (pointerX - weaponBody.getPosition().x + dogeBody.getLinearVelocity().x), (float) (pointerY - weaponBody.getPosition().y) + dogeBody.getLinearVelocity().y);
        weaponBody.setTransform(weaponBody.getPosition().x, weaponBody.getPosition().y, bulletAngle);
        /*if(weaponBody.getLinearVelocity().x > 20 && weaponBody.getLinearVelocity().x > 0)
            weaponBody.setLinearVelocity(20, weaponBody.getLinearVelocity().y);
        else if(weaponBody.getLinearVelocity().x < -20 && weaponBody.getLinearVelocity().x < 0)
            weaponBody.setLinearVelocity(-20, weaponBody.getLinearVelocity().y);*/
    }
}
