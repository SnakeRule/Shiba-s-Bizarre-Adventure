package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Timer;
import com.shibe.game.Components.PlayerComponent;
import com.shibe.game.Managers.Game;
import com.shibe.game.MenuStage;
import com.shibe.game.PlayerData;

import java.io.*;

/**
 * Created by Jere on 22.9.2016.
 */
public class SaveSystem extends EntitySystem implements java.io.Serializable
{
    Timer buttonTimer;
    public static PlayerData playerData = new PlayerData();
    ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    ImmutableArray<Entity> players;
    Entity e = new Entity();


    public SaveSystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    public void Save() {
            try {
                FileOutputStream fileOut = new FileOutputStream("save.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(playerData);
                out.close();

                Game.Save = false;
            }catch (IOException i){
                i.printStackTrace();
            }

        }
    public void Load()
    {
        try {
            FileInputStream fileIn = new FileInputStream("save.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            playerData = (PlayerData) in.readObject();
            in.close();
            fileIn.close();

            Game.Load = false;
        }catch(IOException i) {
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c) {
            System.out.println("class not found");
            c.printStackTrace();
            return;
        }
    }
}
