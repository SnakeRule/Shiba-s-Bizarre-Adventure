package com.shibe.game.Managers;

/**
 * Created by Jere on 2.9.2016.
 */
public class CollisionFilterManager
{
    public static short NONE = 0x000f;
    public static short PLAYER = 0x0064;
    public static short COLLIDE_PROJECTILE = 0x0128;
    public static short NON_COLLIDE_PROJECTILE = 0x0004;
    public static short ENEMY = 0x0008;
    public static short ENEMY_SENSOR = 0x0016;
    public static short CHARACTER_FEET = 0x0032;
}
