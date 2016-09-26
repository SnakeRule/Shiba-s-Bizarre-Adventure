package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.shibe.game.MenuStage;
import com.shibe.game.Systems.SaveSystem;
//import com.shibe.game.Systems.SaveSystem;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Jere on 26.8.2016.
 */
public class PlayerComponent implements Component
{
    public int levelsUnlocked = 2;
    public String name = MenuStage.nameArea.getText();
    public boolean TouchingLadder;
    public Body body;
    public Sprite sprite;
    public boolean canJump;
    public int feetCollisions = 0;
    public int hp = 100;
    public int maxhp = 100;
    public int money = 0;
    public Texture texture;
    public Vector2 SpawnPoint;
    public ConcurrentHashMap<String, Integer> weapons;

    public void setPlayer(boolean left, boolean right, boolean jump, Body body, Fixture feetFixture, Fixture mainFixture, Sprite s, boolean canjump, Texture texture)
    {
        sprite = s;
        boolean moveLeft = left;
        boolean moveRight = right;
        boolean jump1 = jump;
        this.body = body;
        Fixture feetFixture1 = feetFixture;
        Fixture mainFixture1 = mainFixture;
        this.canJump = canjump;
        this.texture = texture;
        SpawnPoint = new Vector2(body.getPosition().x,body.getPosition().y);
        weapons = new ConcurrentHashMap();
        weapons.put("Missile" , 5);

        CheckLoad();
    }

    private void CheckLoad()
    {
        if(SaveSystem.playerData != null)
        {
            money = SaveSystem.playerData.money;
            maxhp = SaveSystem.playerData.maxhp;
            levelsUnlocked = SaveSystem.playerData.levelsCompleted;
        }
    }
}
