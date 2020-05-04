package org.monjasa.engine.levels.iterator;

import com.almasb.fxgl.entity.level.Level;

public interface LevelIterator {

    Level getNext();

    Level getCurrent();

    boolean hasNext();
}
