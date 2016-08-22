package com.shibe.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

/**
 * Created by Jere on 11.8.2016.
 */
public class Character
{
    private Texture dogeImage1;
    private Texture dogeImage2;
    private Texture dogeImage3;
    private float x;
    private float y;
    public TextureMapObject shibeTextureMapObject;
    public Sprite dogeSprite;
    private BodyDef dogeBodyDef;
    private Body dogeBody;
    public Weapon weapon;
    public ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    public int LocationX;
    public int LocationY;
    float size = (float) 1.3;
    private int animationCounter;
    private int fileno = 1;
    private Shape shape;
    private TiledMap mapData;

    public Character(World world, RectangleMapObject rect, TiledMap map)
    {
        mapData = map;
        dogeImage1 = new Texture("SHIBA1.png");
        dogeImage2 = new Texture("SHIBA2.png");
        dogeImage3 = new Texture("SHIBA3.png");
        dogeSprite = new Sprite(dogeImage1);
        dogeSprite.setSize((dogeImage1.getWidth()/size)* Game.WORLD_TO_BOX, (dogeImage1.getHeight()/size)* Game.WORLD_TO_BOX);

        //Create Body
        dogeBodyDef = new BodyDef();
        dogeBodyDef.type = BodyDef.BodyType.DynamicBody;
        dogeBodyDef.fixedRotation = true;
        // set its position

        dogeBodyDef.position.set(new Vector2(rect.getRectangle().getX()* Game.WORLD_TO_BOX, rect.getRectangle().getY()* Game.WORLD_TO_BOX));

        Body dogeBody = world.createBody(dogeBodyDef);

        //Create a Polygon shape for the body
        PolygonShape dogeBox = new PolygonShape();
        dogeBox.setAsBox(dogeSprite.getWidth()/2, dogeSprite.getHeight()/2 - (float)0.01);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dogeBox;
        fixtureDef.density = (float) 1;
        fixtureDef.friction = (float) 0.0;

        //Create fixture
        dogeBody.createFixture(fixtureDef);
        // Clean up
        dogeBox.dispose();
        dogeBody.setUserData(dogeSprite);
    }
    public void Animate(Body dogeBody, boolean rightPressed, boolean leftPressed)
    {
        animationCounter++;

        if(animationCounter >= 8 && (leftPressed == true || rightPressed == true))
        {
            switch (fileno)
            {
                case 1:
                    dogeSprite.setTexture(dogeImage1);
                    break;
                case 2:
                    dogeSprite.setTexture(dogeImage2);
                    break;
                case 3:
                    dogeSprite.setTexture(dogeImage3);
                    break;
            }
            if(fileno < 3)
                fileno++;
            else
                fileno = 1;
            animationCounter = 0;
        }
        if(dogeBody.getLinearVelocity().x > 0 && dogeSprite.isFlipX() == true && rightPressed == true)
        {
            dogeSprite.flip(true,false);
        }
        if(dogeBody.getLinearVelocity().x < 0 && dogeSprite.isFlipX() == false && leftPressed == true)
        {
            dogeSprite.flip(true,false);
        }
    }
}
