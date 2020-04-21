package org.monjasa.engine.levels.tmx;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.LevelLoader;
import com.almasb.fxgl.entity.level.tiled.TiledMap;
import com.almasb.fxgl.entity.level.tiled.TilesetLoader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlatformerTMXLoaderFacade implements LevelLoader {

    @Override
    public Level load(URL url, GameWorld gameWorld) {

        PlatformerTMXLevelParser levelParser = new PlatformerTMXLevelParser();

        try {
            TiledMap levelMap = levelParser.parse(url.openStream());
            TilesetLoader tilesetLoader = new TilesetLoader(levelMap, url);

            PlatformerTMXEntitiesCollector entitiesCollector = new PlatformerTMXEntitiesCollector(levelMap, tilesetLoader, gameWorld);

            List<Entity> tileLayerEntities = entitiesCollector.collectTileLayerEntities();
            List<Entity> objectEntities = entitiesCollector.collectObjectLayerEntities();

            Level level = new Level(
                    levelMap.getWidth() * levelMap.getTilewidth(),
                    levelMap.getHeight() * levelMap.getTileheight(),
                    Stream.of(tileLayerEntities, objectEntities).flatMap(Collection::stream).collect(Collectors.toList()));

            levelMap.getProperties().forEach(level.getProperties()::setValue);

            return level;

        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
