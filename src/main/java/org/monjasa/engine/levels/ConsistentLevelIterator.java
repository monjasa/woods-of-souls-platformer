package org.monjasa.engine.levels;

import com.almasb.fxgl.entity.level.Level;

import java.net.URL;
import java.util.List;

public class ConsistentLevelIterator implements LevelIterator {

    private List<URL> levelURLs;
    private int pos;

    public ConsistentLevelIterator(List<URL> levelURLs) {
        this.levelURLs = levelURLs;
        pos = 0;
    }

    @Override
    public URL getNext() {
        if (hasNext())
            return levelURLs.get(pos++);
        else
            throw new RuntimeException("Out of bounds in Iterator");
    }

    @Override
    public URL getCurrent() {
        return levelURLs.get(pos);
    }

    @Override
    public boolean hasNext() {
        return pos < levelURLs.size();
    }
}
