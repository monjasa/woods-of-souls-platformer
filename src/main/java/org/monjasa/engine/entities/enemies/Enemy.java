package org.monjasa.engine.entities.enemies;

import com.almasb.fxgl.entity.Entity;
import org.monjasa.engine.entities.players.Player;

public abstract class Enemy extends Entity {

    public abstract void hitPlayer(Player player);
}
