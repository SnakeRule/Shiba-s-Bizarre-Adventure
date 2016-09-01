package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.shibe.game.Components.CameraComponent;
import com.shibe.game.Components.CursorComponent;

/**
 * Created by Jere on 30.8.2016.
 */
public class CursorSystem extends EntitySystem
{
    ComponentMapper<CursorComponent> cm = ComponentMapper.getFor(CursorComponent.class);
    ComponentMapper<CameraComponent> cam = ComponentMapper.getFor(CameraComponent.class);
    ImmutableArray<Entity> cursors;
    ImmutableArray<Entity> cameras;
    CursorComponent cursor;
    CameraComponent camera;
    Entity e = new Entity();

    public CursorSystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine)
    {
        super.addedToEngine(engine);
        cursors = engine.getEntitiesFor(Family.all(CursorComponent.class).get());
        e = cursors.get(0);
        cursor = cm.get(e);
        cameras = engine.getEntitiesFor(Family.all(CameraComponent.class).get());
        e = cameras.get(0);
        camera = cam.get(e);
    }

    @Override
    public void update(float deltaTime) {
        cursor.CursorPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.camera.unproject(cursor.CursorPosition);
        cursor.sprite.setPosition(cursor.CursorPosition.x - cursor.sprite.getWidth() / 2, cursor.CursorPosition.y - cursor.sprite.getHeight() / 2);
        super.update(deltaTime);
    }
}
