package org.monjasa.engine.perks;

import com.almasb.fxgl.entity.Entity;

public class EmptyPerk implements Perk {

    @Override
    public boolean execute(Entity receiver) {
        return true;
    }

    @Override
    public void undo(Entity receiver) {
    }
}
