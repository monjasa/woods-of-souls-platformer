package org.monjasa.engine.levels.iterator;

import com.almasb.fxgl.entity.level.Level;
import org.monjasa.engine.entities.PlatformerEntityFactory;

import java.net.URL;
import java.util.List;

public class ConsistentLevelIterator implements LevelIterator {

    private List<URL> levelURLs;
    private PlatformerEntityFactory entityFactories;
    private boolean isDevelopingNewLevel;
    private int pos;

    public ConsistentLevelIterator(List<URL> levelURLs, PlatformerEntityFactory entityFactories, boolean isDevelopingNewLevel) {
        this.levelURLs = levelURLs;
        this.entityFactories = entityFactories;
        this.isDevelopingNewLevel = isDevelopingNewLevel;
        this.pos = 0;
    }

    @Override
    public Level getNext() {
        if (hasNext())
            return entityFactories.createLevel(levelURLs.get(pos++), isDevelopingNewLevel);
        else
            throw new RuntimeException("Out of bounds in Iterator");
    }

    @Override
    public Level getCurrent() {
        return entityFactories.createLevel(levelURLs.get(pos), isDevelopingNewLevel);
    }

    @Override
    public boolean hasNext() {
        return pos < levelURLs.size();
    }
}
