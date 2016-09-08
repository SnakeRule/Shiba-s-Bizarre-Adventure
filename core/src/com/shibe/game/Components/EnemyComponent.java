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
    private boolean jump;
    public Body body;
    private Fixture feetFixture;
    private Fixture LeftSensor;
    private Fixture RightSensor;
    public Sprite sprite;
    private ArrayList<WeaponManager> weapons;
    private boolean canJump;
    public int health;
    public int SpawnTrigger;
    public boolean Triggered;
    public boolean PlayerSpotted;

    public void setEnemy(boolean left, boolean right, boolean jump, Body body, Fixture feetFixture, Sprite s, ArrayList<WeaponManager> weapons, Fixture leftSensor, Fixture rightSensor, boolean canJump, int hp, int spawnTrigger)
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
        SpawnTrigger = spawnTrigger;
        if(SpawnTrigger == 0)
        {
            Triggered = true;
        }
    }
}
