package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.shibe.game.AndroidStage;
import com.shibe.game.Components.CameraComponent;
import com.shibe.game.Components.CursorComponent;
import com.shibe.game.Managers.ActionProcessor;
import com.shibe.game.Managers.Game;

import static com.shibe.game.Managers.Game.AprocessorSet;
import static com.shibe.game.Managers.Game.inputMultiplexer;

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
        if(Game.Android || Game.androidDebug) {
            for (int i = 0; i < 3; i++)
            { // 20 is max number of touch points
                if(Gdx.input.isTouched(i))
                {
                    final int iX = Gdx.input.getX(i);
                    final int iY = Gdx.input.getY(i);
                    if (iX > AndroidStage.leftImage.getX() && iX < AndroidStage.leftImage.getX() + AndroidStage.leftImage.getWidth() && Gdx.graphics.getHeight() - iY > AndroidStage.leftImage.getY() && Gdx.graphics.getHeight() - iY < AndroidStage.leftImage.getY() + AndroidStage.leftImage.getHeight())
                        continue;
                    else if ((iX > AndroidStage.rightImage.getX() && iX < AndroidStage.rightImage.getX() + AndroidStage.rightImage.getWidth() && Gdx.graphics.getHeight() - iY > AndroidStage.rightImage.getY() && Gdx.graphics.getHeight() - iY < AndroidStage.rightImage.getY() + AndroidStage.rightImage.getHeight()))
                        continue;
                    else if ((iX > AndroidStage.upImage.getX() && iX < AndroidStage.upImage.getX() + AndroidStage.upImage.getWidth() && Gdx.graphics.getHeight() - iY > AndroidStage.upImage.getY() && Gdx.graphics.getHeight() - iY < AndroidStage.upImage.getY() + AndroidStage.upImage.getHeight()))
                        continue;
                    else if ((iX > AndroidStage.downImage.getX() && iX < AndroidStage.downImage.getX() + AndroidStage.downImage.getWidth() && Gdx.graphics.getHeight() - iY > AndroidStage.downImage.getY() && Gdx.graphics.getHeight() - iY < AndroidStage.downImage.getY() + AndroidStage.downImage.getHeight()))
                        continue;
                    else
                        cursor.CursorPosition.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                }
                }
            }
        else
            cursor.CursorPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.camera.unproject(cursor.CursorPosition);
        cursor.sprite.setPosition(cursor.CursorPosition.x - cursor.sprite.getWidth() / 2, cursor.CursorPosition.y - cursor.sprite.getHeight() / 2);
        super.update(deltaTime);
    }
}
