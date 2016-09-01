package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Jere on 30.8.2016.
 */
public class CursorComponent implements Component
{
    public Sprite sprite;
    public Vector3 CursorPosition = new Vector3();

    public CursorComponent(Sprite s)
    {
        sprite = s;
    }
}
