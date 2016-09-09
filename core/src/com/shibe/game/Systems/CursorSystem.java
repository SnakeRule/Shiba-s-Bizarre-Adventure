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
    private ComponentMapper<CursorComponent> cm = ComponentMapper.getFor(CursorComponent.class);
    private ComponentMapper<CameraComponent> cam = ComponentMapper.getFor(CameraComponent.class);
    private CursorComponent cursor;
    private CameraComponent camera;

    public CursorSystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine)
    {
        super.addedToEngine(engine);
        ImmutableArray<Entity> cursors = engine.getEntitiesFor(Family.all(CursorComponent.class).get());
        Entity e = cursors.get(0);
        cursor = cm.get(e);
        ImmutableArray<Entity> cameras = engine.getEntitiesFor(Family.all(CameraComponent.class).get());
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
