package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.shibe.game.Components.ItemComponent;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.SpriteComponent;

/**
 * Created by Jere on 16.9.2016.
 */
public class ItemManager
{
    Sprite ItemSprite;
    Body ItemBody;
    //Fixture itemFixture;
    FixtureDef itemFixtureDef;
    PolygonShape polygonShape;
    ItemComponent itemComponent;
    SpriteComponent spriteComponent;
    PhysicsComponent physicsComponent;
    Entity e;
    Texture missileImage;
    Texture foodImage1;
    Texture foodImage2;
    Texture moneyImage1;

    public ItemManager()
    {
        missileImage = new Texture("Rocket.png");
        foodImage1 = new Texture("food1.png");
        foodImage2 = new Texture("Bone.png");
        moneyImage1 = new Texture("Money.png");
    }

    public void createItem(int ItemNmb, float LocationX, float LocationY, World world)
    {
        switch(ItemNmb)
        {
            case 1:
            {
                createMissile();
                break;
            }
            case 2:
            {
                createFood1();
                break;
            }
            case 3:
            {
                createFood2();
                break;
            }
            case 4:
            case 5:
            {
                createMoney();
                break;
            }
        }

        BodyDef ItemBodyDef = new BodyDef();
        ItemBodyDef.position.set(LocationX, LocationY);
        ItemBodyDef.type = BodyDef.BodyType.DynamicBody;

        ItemBody = world.createBody(ItemBodyDef);

        itemComponent.setBody(ItemBody);
        itemComponent.setSprite(ItemSprite);

        if(ItemNmb > 0 && ItemNmb < 4) {
            itemFixtureDef = new FixtureDef();
            polygonShape = new PolygonShape();
            polygonShape.setAsBox(ItemSprite.getWidth() / 2, ItemSprite.getHeight() / 2);
            itemFixtureDef.shape = polygonShape;
        }
        if(ItemNmb == 4 || ItemNmb == 5) {
            CircleShape circle = new CircleShape();
            circle.setRadius(ItemSprite.getWidth()/2);
            ItemBody.setTransform(ItemBody.getPosition().x + MathUtils.random((float) -0.3, (float) 0.3),ItemBody.getPosition().y + MathUtils.random((float) -0.3, (float) 0.3), 0);

            itemFixtureDef = new FixtureDef();
            itemFixtureDef.shape = circle;
            itemFixtureDef.density = 0.6f;
            itemFixtureDef.friction = 0.5f;
            itemFixtureDef.restitution = 0.4f; // Make it bounce a little bit
        }

        Fixture fixture = ItemBody.createFixture(itemFixtureDef);
        Filter filter = new Filter();
        filter.categoryBits = CollisionFilterManager.ITEM;
        filter.maskBits = (short) (~CollisionFilterManager.ENEMY & ~CollisionFilterManager.ENEMY_SENSOR & ~CollisionFilterManager.NON_COLLIDE_PROJECTILE & ~CollisionFilterManager.COLLIDE_PROJECTILE);

        fixture.setFilterData(filter);

        spriteComponent = new SpriteComponent();
        spriteComponent.setSprite(ItemSprite);

        physicsComponent = new PhysicsComponent();
        physicsComponent.setBody(ItemBody);
        ItemBody.setUserData("Item");
        ItemBody.setLinearVelocity(0,1);

        e = new Entity();
        e.add(itemComponent);
        e.add(spriteComponent);
        e.add(physicsComponent);

        fixture.setUserData(e);

        Game.engine.addEntity(e);
    }

    public void createMissile()
    {
        ItemSprite = new Sprite(missileImage);
        ItemSprite.setSize(ItemSprite.getWidth() * Game.WORLD_TO_BOX / 8, ItemSprite.getHeight() * Game.WORLD_TO_BOX / 8);
        ItemSprite.setOrigin(ItemSprite.getWidth() / 2, ItemSprite.getHeight() / 2);

        itemComponent = new ItemComponent();
        itemComponent.setBullets(5);
        itemComponent.setItemID(1);
    }

    public void createFood1()
    {
        ItemSprite = new Sprite(foodImage1);
        ItemSprite.setSize(ItemSprite.getWidth() * Game.WORLD_TO_BOX, ItemSprite.getHeight() * Game.WORLD_TO_BOX);
        ItemSprite.setOrigin(ItemSprite.getWidth() / 2, ItemSprite.getHeight() / 2);

        itemComponent = new ItemComponent();
        itemComponent.setHealthRestored(20);
        itemComponent.setItemID(2);
    }
    public void createFood2()
    {
        ItemSprite = new Sprite(foodImage2);
        ItemSprite.setSize(ItemSprite.getWidth() * Game.WORLD_TO_BOX, ItemSprite.getHeight() * Game.WORLD_TO_BOX);
        ItemSprite.setOrigin(ItemSprite.getWidth() / 2, ItemSprite.getHeight() / 2);

        itemComponent = new ItemComponent();
        itemComponent.setHealthRestored(50);
        itemComponent.setItemID(3);
    }

    public void createMoney()
    {
            ItemSprite = new Sprite(moneyImage1);
            ItemSprite.setSize((float) (ItemSprite.getWidth() * Game.WORLD_TO_BOX / 1.5), (float) (ItemSprite.getHeight() * Game.WORLD_TO_BOX / 1.5));
            ItemSprite.setOrigin(ItemSprite.getWidth() / 2, ItemSprite.getHeight() / 2);

            itemComponent = new ItemComponent();
            itemComponent.setMoneyValue(1);
            itemComponent.setItemID(4);
    }
}
