package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.PositionComponent;
import com.shibe.game.Components.SpriteComponent;
import com.shibe.game.Managers.Game;

/**
 * Created by Jere on 26.8.2016.
 */
public class MovementSystem extends EntitySystem
{
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PhysicsComponent> pc = ComponentMapper.getFor(PhysicsComponent.class);
    private ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);

    public MovementSystem() {
        super();
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, PhysicsComponent.class).get());
    }

    public void update(float deltaTime) {
        if (!Game.pause) {
            for (int i = 0; i < entities.size(); ++i) {
                Entity entity = entities.get(i);
                SpriteComponent sprite = sm.get(entity);
                PhysicsComponent physics = pc.get(entity);

                sprite.sprite.setPosition(physics.body.getPosition().x - sprite.sprite.getWidth() / 2, physics.body.getPosition().y - sprite.sprite.getHeight() / 2);
                sprite.sprite.setRotation(MathUtils.radiansToDegrees * physics.body.getAngle());
            }
        }
    }
}
