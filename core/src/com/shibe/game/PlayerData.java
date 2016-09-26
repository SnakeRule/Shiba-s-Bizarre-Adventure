package com.shibe.game;

import com.shibe.game.Components.PlayerComponent;

/**
 * Created by Jere on 22.9.2016.
 */
public class PlayerData implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int hp;
    public int maxhp = 100;
    public String name = "Doge";
    public int levelsCompleted = 1;
    public int money = 0;

    public void Reset()
    {
        maxhp = 100;
        name = "Doge";
        levelsCompleted = 1;
        money = 0;
    }
}
