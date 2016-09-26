package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.ObjectComponent;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.PositionComponent;
import com.shibe.game.Components.SpriteComponent;
import com.shibe.game.Managers.Game;

/**
 * Created by Jere on 12.8.2016.
 */
public class InteractableObjectManager
{
    private Body objectBody;
    private String Nmb;
    private Boolean Moving = false;
    private Boolean hasSprite;
    private Boolean doorUp = false;
    private int moveCounter;
    Texture doorImage = new Texture("Door.png");
    Texture buttonTexture = new Texture("Button.png");
    Sprite objectSprite;


    public void newObject(World world, Shape shape, MapObject object)
    {
        Entity e = new Entity();
        SpriteComponent spriteComponent = new SpriteComponent();
        PhysicsComponent physicsComponent = new PhysicsComponent();
        ObjectComponent objectComponent = new ObjectComponent();
        String objectname = "";

            if(object.getName().equals("Door")) {
                RectangleMapObject rect = (RectangleMapObject) object;
                objectSprite = new Sprite(doorImage);
                objectname = object.getName();
                Nmb =object.getProperties().get("Link").toString();
                objectSprite.setTexture(doorImage);
                objectSprite.setSize(rect.getRectangle().getWidth() * Game.WORLD_TO_BOX, rect.getRectangle().getHeight() * Game.WORLD_TO_BOX);


                BodyDef objectBodyDef = new BodyDef();
                objectBodyDef.type = BodyDef.BodyType.KinematicBody;
                objectBodyDef.position.set(new Vector2(rect.getRectangle().getX() * Game.WORLD_TO_BOX + (objectSprite.getWidth() / 2), (rect.getRectangle().getY() * Game.WORLD_TO_BOX) + (objectSprite.getHeight() / 2)));

                objectBody = world.createBody(objectBodyDef);
                objectBody.setAwake(false);

                PolygonShape objectBox = new PolygonShape();
                objectBox.setAsBox(objectSprite.getWidth() / 2, objectSprite.getHeight() / 2);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = objectBox;
                fixtureDef.density = 1;

                Fixture fixture = objectBody.createFixture(fixtureDef);
                fixture.setUserData("Door");
                shape.dispose();

                hasSprite = true;
            }

            if(object.getName().equals("Button"))
            {
                Nmb = object.getProperties().get("Link").toString();
                EllipseMapObject circle = (EllipseMapObject) object;
                objectname = object.getName();
                objectSprite = new Sprite(buttonTexture);
                TextureRegion objectRegion = new TextureRegion();
                objectRegion.setTexture(buttonTexture);
                objectRegion.setRegion(0,0, buttonTexture.getWidth()/4, buttonTexture.getHeight());
                objectSprite = new Sprite(objectRegion);
                objectSprite.setSize((float) (objectSprite.getWidth()*1.2 * Game.WORLD_TO_BOX), (float) (objectSprite.getHeight()*1.2 * Game.WORLD_TO_BOX));

                BodyDef objectBodyDef = new BodyDef();
                objectBodyDef.type = BodyDef.BodyType.KinematicBody;
                objectBodyDef.position.set(new Vector2(circle.getEllipse().x *Game.WORLD_TO_BOX +(objectSprite.getWidth() / 2), (circle.getEllipse().y *Game.WORLD_TO_BOX)));

                objectBody = world.createBody(objectBodyDef);

                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(objectSprite.getWidth()/2);
                circleShape.setPosition(new Vector2(0, 0 - objectSprite.getHeight()/4));

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = circleShape;
                fixtureDef.density = 1;

                Fixture fixture = objectBody.createFixture(fixtureDef);
                fixture.setUserData("Button");
                shape.dispose();

                hasSprite = true;
            }

        if(object.getName().equals("Ladder"))
        {

            objectname = object.getName();
            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            objectBody = world.createBody(bd);
            objectBody.setAwake(false);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;
            Fixture fixture = objectBody.createFixture(fixtureDef);
            fixture.setUserData("Ladder");

            shape.dispose();

            hasSprite = false;
        }

        if(object.getName().equals("Teleport"))
        {

            Nmb = object.getProperties().get("Link").toString();
            objectname = object.getName();
            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            objectBody = world.createBody(bd);
            objectBody.setAwake(false);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            Fixture fixture = objectBody.createFixture(fixtureDef);
            fixture.setUserData("Teleport");

            shape.dispose();

            hasSprite = false;
        }

        if(hasSprite) {
            spriteComponent.setSprite(objectSprite);
            objectComponent.setSprite(objectSprite);
            e.add(spriteComponent);
        }

        if(Nmb != null)
            objectComponent.setNmb(Nmb);

        objectComponent.setBody(objectBody);
        objectComponent.setObjectName(objectname);
        objectComponent.setSpawnY(objectBody.getPosition().y);
        e.add(objectComponent);

        objectBody.setUserData(e);
        physicsComponent.setBody(objectBody);
        e.add(physicsComponent);

        objectBody.setUserData(e);
        Game.engine.addEntity(e);
    }
}
