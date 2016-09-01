package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
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
    private float x;
    private float y;
    public TextureMapObject shibeTextureMapObject;
    Sprite dogeSprite;
    private BodyDef dogeBodyDef;
    Body dogeBody;
    public WeaponManager weapon;
    public int footContacts = 0;
    ArrayList<WeaponManager> weapons = new ArrayList<WeaponManager>();
    private float size = (float) 1.6;
    protected int animationCounter;
    protected int fileno = 1;
    boolean moveLeft;
    boolean moveRight;
    boolean Jump;
    Fixture feetFixture;
    Fixture fixture;
    private TiledMap mapData;
    boolean canJump;
    Entity e = new Entity();


    CharacterManager(World world, RectangleMapObject rect, TiledMap map)
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

        dogeBody = world.createBody(dogeBodyDef);

        //Create a Polygon shape for the body
        PolygonShape dogeBox = new PolygonShape();
        dogeBox.setAsBox(dogeSprite.getWidth()/2, dogeSprite.getHeight()/2 - (float)0.01);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dogeBox;
        fixtureDef.density = (float) 1.5;
        fixtureDef.friction = (float) 0;
        fixtureDef.filter.categoryBits = 0x0008;
        fixtureDef.filter.maskBits = 0x000f;

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
        feetFixtureDef.filter.categoryBits = 0x0001;
        feetFixtureDef.filter.maskBits = 0x000f & ~0x0002;

        feetFixture = dogeBody.createFixture(feetFixtureDef);
        feetFixture.setUserData("Feet");
    }
}
