package com.shibe.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Jere on 12.8.2016.
 */
public class InteractableObject
{
    private Texture objectImage;
    public TextureRegion objectRegion;
    public Sprite doorSprite = new Sprite();
    public Sprite buttonSprite = new Sprite();
    public Body doorBody;
    public String Nmb = "";
    public String objectname = "";
    public Boolean Moving = false;
    private Boolean doorUp = false;
    private int moveCounter;
    RectangleMapObject rect;
    EllipseMapObject circle;

    public InteractableObject(World world, Shape shape, MapObject object)
    {
        {
            if(object.getName().equals("Door")) {
                rect = (RectangleMapObject) object;
                objectImage = new Texture("Door.png");
                objectname = object.getName();
                Nmb = object.getProperties().get("Link").toString();
                doorSprite = new Sprite(objectImage);
                doorSprite.setSize(rect.getRectangle().getWidth() * Game.WORLD_TO_BOX, rect.getRectangle().getHeight() * Game.WORLD_TO_BOX);

                BodyDef objectBodyDef = new BodyDef();
                objectBodyDef.type = BodyDef.BodyType.KinematicBody;
                objectBodyDef.position.set(new Vector2(rect.getRectangle().getX() * Game.WORLD_TO_BOX + (doorSprite.getWidth() / 2), (rect.getRectangle().getY() * Game.WORLD_TO_BOX) + (doorSprite.getHeight() / 2)));

                doorBody = world.createBody(objectBodyDef);

                PolygonShape objectBox = new PolygonShape();
                objectBox.setAsBox(doorSprite.getWidth() / 2, doorSprite.getHeight() / 2);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = objectBox;
                fixtureDef.density = 1;

                Fixture fixture = doorBody.createFixture(fixtureDef);
                fixture.setUserData(Nmb);
                shape.dispose();
                doorBody.setUserData(doorSprite);
            }
            if(object.getName().equals("Button"))
            {
                Nmb = object.getProperties().get("Link").toString();
                circle = (EllipseMapObject) object;
                objectname = object.getName();
                objectImage = new Texture("Button.png");
                objectRegion = new TextureRegion();
                objectRegion.setTexture(objectImage);
                objectRegion.setRegion(0,0,objectImage.getWidth()/4,objectImage.getHeight());
                buttonSprite = new Sprite(objectRegion);
                buttonSprite.setSize((float) (buttonSprite.getWidth()*1.2 * Game.WORLD_TO_BOX), (float) (buttonSprite.getHeight()*1.2 * Game.WORLD_TO_BOX));

                BodyDef objectBodyDef = new BodyDef();
                objectBodyDef.type = BodyDef.BodyType.KinematicBody;
                objectBodyDef.position.set(new Vector2(circle.getEllipse().x *Game.WORLD_TO_BOX +(buttonSprite.getWidth() / 2), (circle.getEllipse().y *Game.WORLD_TO_BOX)));

                Body buttonBody = world.createBody(objectBodyDef);

                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(buttonSprite.getWidth()/2);
                circleShape.setPosition(new Vector2(0, 0 - buttonSprite.getHeight()/4));

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = circleShape;
                fixtureDef.density = 1;

                Fixture fixture = buttonBody.createFixture(fixtureDef);
                fixture.setUserData(Nmb);
                shape.dispose();
                buttonBody.setUserData(buttonSprite);
            }
        }

        if(object.getName().equals("Ladder"))
        {
            objectname = object.getName();
            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bd);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            fixtureDef.filter.groupIndex = -8;
            body.createFixture(fixtureDef);
            body.setUserData(objectname);

            shape.dispose();
        }
        /*floorImage = new Texture("Block"+fileno+".png");
        floorSprite = new Sprite(floorImage);
        floorSprite.setSize(floorImage.getWidth()* Game.WORLD_TO_BOX, floorImage.getHeight()* Game.WORLD_TO_BOX);
        floorSprite.setOrigin(floorImage.getWidth()/2* Game.WORLD_TO_BOX, floorImage.getHeight()/2* Game.WORLD_TO_BOX);

        BodyDef blockBodyDef = new BodyDef();
        blockBodyDef.type = BodyDef.BodyType.StaticBody;
        blockBodyDef.position.set(new Vector2(x* Game.WORLD_TO_BOX,y* Game.WORLD_TO_BOX));

        Body blockBody = world.createBody(blockBodyDef);

        PolygonShape blockBox = new PolygonShape();
        blockBox.setAsBox(floorSprite.getWidth()/2,floorSprite.getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();

        blockBody.createFixture(blockBox, 0);
        blockBox.dispose();
        blockBody.setUserData(floorSprite);*/
    }

    public void DoorMove(final Body body) {
            if (doorUp == false && Moving == false) {
                body.setLinearVelocity(0, 10);
                Moving = true;
                Timer.schedule(new Timer.Task() {

                    @Override
                    public void run() {
                        body.setLinearVelocity(0, 0);
                        doorUp = true;
                        Moving = false;
                    }
                }, (float) 0.3);
            } else if (doorUp == true && Moving == false) {
                Moving = true;
                body.setLinearVelocity(0, -10);
                Timer.schedule(new Timer.Task() {

                    @Override
                    public void run() {
                        body.setLinearVelocity(0, 0);
                        doorUp = false;
                        Moving = false;
                    }
                }, (float) 0.3);
            }
        }
    }
