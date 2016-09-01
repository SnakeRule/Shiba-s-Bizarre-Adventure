package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.shibe.game.Components.PositionComponent;
import com.shibe.game.Components.SpriteComponent;

/**
 * Created by Jere on 26.8.2016.
 */
public class DrawSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);

    public DrawSystem() {
        super();
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, SpriteComponent.class).get());
    }

    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            PositionComponent position = pm.get(entity);
            SpriteComponent sprite = sm.get(entity);

            sprite.sprite.setX(position.x - sprite.sprite.getWidth()/2);
            sprite.sprite.setY(position.y - sprite.sprite.getHeight()/2);
            sprite.sprite.setRotation((MathUtils.radiansToDegrees * position.angle));
        }
    }
}
