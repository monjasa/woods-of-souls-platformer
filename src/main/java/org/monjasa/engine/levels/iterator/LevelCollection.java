package org.monjasa.engine.levels.iterator;

import org.monjasa.engine.entities.PlatformerEntityFactory;

import java.net.URL;
import java.util.List;

public class LevelCollection implements Collection {

    private List<URL> levelURLs;
    private PlatformerEntityFactory entityFactories;
    private boolean isDevelopingNewLevel;

    public LevelCollection(List<URL> levelURLs, PlatformerEntityFactory entityFactories, boolean isDevelopingNewLevel) {
        this.levelURLs = levelURLs;
        this.entityFactories = entityFactories;
        this.isDevelopingNewLevel = isDevelopingNewLevel;
    }

    @Override
    public LevelIterator createConsistentLevelIterator() {
        return new ConsistentLevelIterator(levelURLs, entityFactories, isDevelopingNewLevel);
    }
}
