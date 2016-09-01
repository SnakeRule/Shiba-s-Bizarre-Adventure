package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by Jere on 27.8.2016.
 */
public class CameraComponent implements Component
{
    public OrthographicCamera camera;

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }
}
