package org.monjasa.engine.levels;

import com.almasb.fxgl.entity.level.Level;

import java.net.URL;

public interface LevelIterator {

    URL getNext();

    URL getCurrent();

    boolean hasNext();
}
