package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.SpriteComponent;
import com.shibe.game.Components.TreasureComponent;

/**
 * Created by Jere on 16.9.2016.
 */
public class TreasureManager
{
    private Texture treasureImage1_closed = new Texture("chest1_closed.png");
    private Texture treasureImage1_open = new Texture("chest1_open.png");
    private Sprite treasureSprite;
    private int Type;
    private Body treasureBody;
    private SpriteComponent spriteComponent;
    private PhysicsComponent physicsComponent;
    private TreasureComponent treasureComponent;
    private Entity e;

    public void CreateTreasure(World world, Shape shape, MapObject object)
    {
        e = new Entity();

        RectangleMapObject rect = (RectangleMapObject) object;

        treasureSprite = new Sprite(treasureImage1_closed);
        treasureSprite.setSize((float) (treasureSprite.getWidth() * Game.WORLD_TO_BOX / 1.5), (float) (treasureSprite.getHeight() * Game.WORLD_TO_BOX / 1.5));

        Type = Integer.parseInt(object.getProperties().get("Type").toString());


        BodyDef treasureBodyDef = new BodyDef();
        treasureBodyDef.type = BodyDef.BodyType.StaticBody;
        treasureBodyDef.position.set(new Vector2(rect.getRectangle().getX() * Game.WORLD_TO_BOX , (rect.getRectangle().getY() + rect.getRectangle().getHeight() / 2) * Game.WORLD_TO_BOX));

        treasureBody = world.createBody(treasureBodyDef);

        PolygonShape objectBox = new PolygonShape();
        objectBox.setAsBox(treasureSprite.getWidth() / 2, treasureSprite.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = objectBox;
        fixtureDef.density = 1;

        Fixture fixture = treasureBody.createFixture(fixtureDef);
        fixture.setUserData("Treasure");
        shape.dispose();

        spriteComponent = new SpriteComponent();
        spriteComponent.setSprite(treasureSprite);
        physicsComponent = new PhysicsComponent();
        physicsComponent.setBody(treasureBody);
        treasureComponent = new TreasureComponent();
        treasureComponent.setType(Type);
        treasureComponent.setSprite(treasureSprite);
        treasureComponent.setClosed(treasureImage1_closed);
        treasureComponent.setOpened(treasureImage1_open);

        e.add(spriteComponent);
        e.add(physicsComponent);
        e.add(treasureComponent);

        treasureBody.setUserData(e);

        Game.engine.addEntity(e);
    }
}
