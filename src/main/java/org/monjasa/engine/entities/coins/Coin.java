package org.monjasa.engine.entities.coins;

import com.almasb.fxgl.entity.Entity;

public abstract class Coin extends Entity {

    public abstract void onCollected();
}
