package com.shibe.game.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.shibe.game.Components.CameraComponent;

import static com.shibe.game.Managers.Game.engine;

/**
 * Created by Jere on 30.8.2016.
 */
class CameraManager
{
    Entity cameraEntity = new Entity();
    CameraComponent cameraComponent = new CameraComponent();
    OrthographicCamera camera = new OrthographicCamera();

    public void initCamera()
    {
        camera.setToOrtho(false, (float) (16f/1.7), (float) (9f/1.7));
        cameraComponent.setCamera(camera);
        cameraEntity.add(cameraComponent);
        engine.addEntity(cameraEntity);
    }
}
