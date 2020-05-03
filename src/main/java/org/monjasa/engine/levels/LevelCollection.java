package org.monjasa.engine.levels;

import java.net.URL;
import java.util.List;

public class LevelCollection implements Collection {

    private List<URL> levelURLs;

    public LevelCollection(List<URL> levelURLs) {
        this.levelURLs = levelURLs;
    }

    @Override
    public LevelIterator createConsistentLevelIterator() {
        return new ConsistentLevelIterator(levelURLs);
    }
}
