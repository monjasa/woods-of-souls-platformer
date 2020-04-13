package org.monjasa.engine.entities.players;

import com.almasb.fxgl.entity.Entity;

public abstract class Player extends Entity {

    public abstract void goLeft();

    public abstract void goRight();

    public abstract void horizontalStop();

    public abstract void goUp();
}
