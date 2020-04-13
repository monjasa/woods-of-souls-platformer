package org.monjasa.engine.entities.players;

import com.almasb.fxgl.entity.Entity;

public abstract class Player extends Entity {

    public abstract void moveLeft();

    public abstract void moveRight();

    public abstract void stopHorizontal();

    public abstract void jump();
}
