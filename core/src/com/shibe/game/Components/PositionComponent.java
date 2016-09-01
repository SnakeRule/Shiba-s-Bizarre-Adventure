package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Jere on 26.8.2016.
 */
public class PositionComponent implements Component
{
    public float x;
    public float y;
    public float angle;

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setAngle(float a){
        this.angle = a;
    }
}
