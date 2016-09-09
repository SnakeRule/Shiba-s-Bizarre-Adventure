package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.shibe.game.Components.WeaponComponent;

/**
 * Created by Jere on 1.9.2016.
 */
public class WeaponSystem extends EntitySystem
{
    private ComponentMapper<WeaponComponent> wm = ComponentMapper.getFor(WeaponComponent.class);
    private ImmutableArray<Entity> weapons;
    Entity e;

    public WeaponSystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        weapons = engine.getEntitiesFor(Family.all(WeaponComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for(int i = 0; i < weapons.size(); i++)
        {
            e = weapons.get(i);
            WeaponComponent weapon = wm.get(e);
            weapon.timer++;

            if(weapon.Delete)
            {
                DestroySystem.BodyDestroyList.add(weapon.body);
                DestroySystem.EntityDestroyList.add(e);
            }
            if(weapon.timer > 500)
                weapon.Delete = true;
        }

    }
}
