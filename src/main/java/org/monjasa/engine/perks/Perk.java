package org.monjasa.engine.perks;

import java.io.Serializable;

public interface Perk extends Serializable {

    boolean execute();

    void undo();
}
