package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by Jere on 26.8.2016.
 */
public class RenderComponent implements Component
{
    public OrthogonalTiledMapRenderer renderer;
    public SpriteBatch batch;

    public void setRenderer(OrthogonalTiledMapRenderer r){this.renderer = r;}

    public void setBatch(SpriteBatch s){this.batch = s;}
}
