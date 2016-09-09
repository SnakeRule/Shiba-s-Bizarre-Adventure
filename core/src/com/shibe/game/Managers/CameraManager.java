package com.shibe.game.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.shibe.game.Components.CameraComponent;

/**
 * Created by Jere on 30.8.2016.
 */
class CameraManager
{

    public CameraManager(Engine engine)
    {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, 16f, 9f);
        CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.setCamera(camera);
        Entity cameraEntity = new Entity();
        cameraEntity.add(cameraComponent);
        engine.addEntity(cameraEntity);
    }
}
