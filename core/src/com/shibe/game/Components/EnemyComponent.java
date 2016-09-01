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
    public boolean jump;
    public Body body;
    public Fixture feetFixture;
    public Fixture LeftSensor;
    public Fixture RightSensor;
    public Sprite sprite;
    public ArrayList<WeaponManager> weapons;
    public boolean canJump;
    public int health;

    public void setEnemy(boolean left, boolean right, boolean jump, Body body, Fixture feetFixture, Sprite s, ArrayList<WeaponManager> weapons, Fixture leftSensor, Fixture rightSensor, boolean canJump, int hp)
    {
        sprite = s;
        moveLeft = left;
        moveRight = right;
        this.jump = jump;
        this.body = body;
        this.feetFixture = feetFixture;
        this.weapons = weapons;
        LeftSensor = leftSensor;
        RightSensor = rightSensor;
        this.canJump = canJump;
        health = hp;
    }
}