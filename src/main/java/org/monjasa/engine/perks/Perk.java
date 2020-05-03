package org.monjasa.engine.perks;

import com.almasb.fxgl.entity.Entity;

import java.io.Serializable;

public interface Perk extends Serializable {

    boolean execute(Entity receiver);

    void undo(Entity receiver);
}
