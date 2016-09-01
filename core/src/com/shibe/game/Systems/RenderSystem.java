package com.shibe.game.Systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.shibe.game.Components.*;

/**
 * Created by Jere on 26.8.2016.
 */
public class RenderSystem extends EntitySystem
{
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> renderEntities;
    private ImmutableArray<Entity> cameraEntities;
    private ImmutableArray<Entity> worldEntities;
    private ImmutableArray<Entity> players;

    private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);
    private ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private ComponentMapper<CameraComponent> cm = ComponentMapper.getFor(CameraComponent.class);
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);

    public RenderSystem(){
        super();
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.one(SpriteComponent.class).get());
        renderEntities = engine.getEntitiesFor(Family.all(RenderComponent.class).get());
        cameraEntities = engine.getEntitiesFor(Family.all(CameraComponent.class).get());
        worldEntities = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    public void update(float deltaTime) {
        Entity renderEntity = renderEntities.get(0);
        RenderComponent render = rm.get(renderEntity);
        Entity cameraEntity = cameraEntities.get(0);
        CameraComponent camera = cm.get(cameraEntity);
        Entity worldEntity = worldEntities.get(0);
        WorldComponent world = wm.get(worldEntity);
        Entity playerEntity = players.get(0);
        PlayerComponent player = pm.get(playerEntity);

        camera.camera.position.set(player.sprite.getX(), (float) 4.5, 0);
        camera.camera.update();
        render.renderer.setView(camera.camera);
        render.renderer.render();
        render.batch.setProjectionMatrix(camera.camera.combined);
        render.batch.begin();
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            SpriteComponent sprite = sm.get(entity);
            sprite.sprite.draw(render.batch);
        }
        render.batch.end();
        world.debugRenderer.render(world.world, camera.camera.combined);
        world.world.step(1 / 60f, 6, 2);
    }
}
