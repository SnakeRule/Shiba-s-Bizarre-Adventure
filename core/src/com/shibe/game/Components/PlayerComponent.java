package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.shibe.game.Managers.WeaponManager;

import java.util.ArrayList;

/**
 * Created by Jere on 26.8.2016.
 */
public class PlayerComponent implements Component
{
    private boolean moveLeft;
    private boolean moveRight;
    private boolean jump;
    public boolean TouchingLadder;
    public Body body;
    private Fixture feetFixture;
    private Fixture mainFixture;
    public Sprite sprite;
    public ArrayList<WeaponManager> weapons;
    public boolean canJump;
    public int feetCollisions = 0;
    public Texture texture1;
    public Texture texture2;
    public Texture texture3;
    public Vector2 SpawnPoint;

    public void setPlayer(boolean left, boolean right, boolean jump, Body body, Fixture feetFixture, Fixture mainFixture, Sprite s, ArrayList<WeaponManager> weapons, boolean canjump, Texture texture1, Texture texture2, Texture texture3)
    {
        sprite = s;
        moveLeft = left;
        moveRight = right;
        this.jump = jump;
        this.body = body;
        this.feetFixture = feetFixture;
        this.mainFixture = mainFixture;
        this.weapons = weapons;
        this.canJump = canjump;
        this.texture1 = texture1;
        this.texture2 = texture2;
        this.texture3 = texture3;
        SpawnPoint = new Vector2(body.getPosition().x,body.getPosition().y);
    }
}
