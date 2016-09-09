package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.shibe.game.Managers.WeaponManager;

import java.util.ArrayList;

/**
 * Created by Jere on 30.8.2016.
 */
public class EnemyComponent implements Component
{
    public boolean moveLeft;
    public boolean moveRight;
    public Body body;
    public Sprite sprite;
    public int health;
    public boolean PlayerSpotted;

    public void setEnemy(boolean left, boolean right, boolean jump, Body body, Fixture feetFixture, Sprite s, Fixture leftSensor, Fixture rightSensor, boolean canJump, int hp)
    {
        sprite = s;
        moveLeft = left;
        moveRight = right;
        boolean jump1 = jump;
        this.body = body;
        Fixture feetFixture1 = feetFixture;
        Fixture leftSensor1 = leftSensor;
        Fixture rightSensor1 = rightSensor;
        boolean canJump1 = canJump;
        health = hp;
    }
}
