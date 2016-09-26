package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.shibe.game.Components.SpriteComponent;
import com.shibe.game.Components.WeaponComponent;

/**
 * Created by Jere on 1.9.2016.
 */
public class WeaponSystem extends EntitySystem
{
    private ComponentMapper<WeaponComponent> wm = ComponentMapper.getFor(WeaponComponent.class);
    private ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private ImmutableArray<Entity> weapons;
    private WeaponComponent weapon;
    private SpriteComponent sprite;
    private Entity e;

    public WeaponSystem() {
        super();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        weapons = engine.getEntitiesFor(Family.all(WeaponComponent.class, SpriteComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for(int i = 0; i < weapons.size(); i++)
        {
            e = weapons.get(i);
            weapon = wm.get(e);
            sprite = sm.get(e);

            if(weapon.Delete)
            {
                DestroySystem.BodyDestroyList.add(weapon.body);
                DestroySystem.EntityDestroyList.add(e);
                DestroySystem.TextureDestroyList.add(sprite.sprite.getTexture());
            }
        }

    }
}
