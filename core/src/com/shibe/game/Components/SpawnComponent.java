package com.shibe.game.Components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Jere on 7.9.2016.
 */
public class SpawnComponent  implements Component
{
    public int spawnType;
    public float spawnX;
    public float spawnY;
    public int spawnLink;
    public boolean spawn;

    public void setSpawn(int spawnType, float spawnX, float spawnY, int spawnLink)
    {
        this.spawnType = spawnType;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnLink = spawnLink;
    }
}
