package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.shibe.game.Components.PhysicsComponent;
import com.shibe.game.Components.PositionComponent;

/**
 * Created by Jere on 26.8.2016.
 */
public class MovementSystem extends EntitySystem
{
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<PhysicsComponent> pc = ComponentMapper.getFor(PhysicsComponent.class);

    public MovementSystem() {
        super();
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class).get());
    }

    public void update(float deltaTime) {

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            PositionComponent position = pm.get(entity);
            PhysicsComponent physics = pc.get(entity);
            //VelocityComponent velocity = vm.get(entity);

            position.x = physics.body.getPosition().x;
            position.y = physics.body.getPosition().y;
            position.angle = physics.body.getAngle();
        }
    }
}
