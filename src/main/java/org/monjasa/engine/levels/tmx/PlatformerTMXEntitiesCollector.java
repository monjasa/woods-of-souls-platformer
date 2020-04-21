package org.monjasa.engine.levels.tmx;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.entity.level.tiled.TiledMap;
import com.almasb.fxgl.entity.level.tiled.TilesetLoader;

import java.util.List;
import java.util.stream.Collectors;

class PlatformerTMXEntitiesCollector {

    private TiledMap tiledMap;
    private TilesetLoader tilesetLoader;
    private GameWorld gameWorld;

    PlatformerTMXEntitiesCollector(TiledMap tiledMap, TilesetLoader tilesetLoader, GameWorld gameWorld) {
        this.tiledMap = tiledMap;
        this.tilesetLoader = tilesetLoader;
        this.gameWorld = gameWorld;
    }

    List<Entity> collectTileLayerEntities() {

        return tiledMap.getLayers().stream()
                .filter(layer -> layer.getType().equals("tilelayer"))
                .map(layer -> {
                    Entity layerEntity = new Entity();
                    layerEntity.getViewComponent().addChild(tilesetLoader.loadView(layer.getName()));
                    return layerEntity;
                }).collect(Collectors.toList());
    }

    List<Entity> collectObjectLayerEntities() {

        return tiledMap.getLayers().stream()
                .filter(layer -> layer.getType().equals("objectgroup"))
                .flatMap(layer -> layer.getObjects().stream())
                .map(tiledObject -> {

                    SpawnData data = new SpawnData(
                            tiledObject.getX(),
                            tiledObject.getY() - (tiledObject.getGid() == 0 ? 0 : tiledObject.getHeight())
                    );

                    data.put("name", tiledObject.getName());
                    data.put("type", tiledObject.getType());
                    data.put("width", tiledObject.getWidth());
                    data.put("height", tiledObject.getHeight());
                    data.put("rotation", tiledObject.getRotation());
                    data.put("id", tiledObject.getId());
                    data.put("gid", tiledObject.getGid());

                    tiledObject.getProperties().forEach(data::put);

                    Entity entity = gameWorld.create(tiledObject.getType(), data);
                    data.getData().forEach(entity::setProperty);

                    entity.addComponent(new IDComponent(tiledObject.getName(), tiledObject.getId()));

                    entity.setPosition(data.getX(), data.getY());
                    entity.setRotation(tiledObject.getRotation());

                    if (tiledObject.getGid() != 0) {
                        entity.getViewComponent().addChild(tilesetLoader.loadView(
                                tiledObject.getGid(),
                                tiledObject.isFlippedHorizontal(),
                                tiledObject.isFlippedVertical()
                        ));
                    }

                    return entity;
                }).collect(Collectors.toList());
    }
}
