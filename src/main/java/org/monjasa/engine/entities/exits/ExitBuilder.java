package org.monjasa.engine.entities.exits;

import org.monjasa.engine.entities.PlatformerEntityBuilder;
import org.monjasa.engine.entities.factories.PlatformerEntityFactory;
import org.monjasa.engine.entities.players.Player;

public class ExitBuilder extends PlatformerEntityBuilder<ExitBuilder, Exit> {

    public ExitBuilder(PlatformerEntityFactory factory) {
        super(factory);
        entity = factory.getExitInstance();
    }

    @Override
    protected ExitBuilder getThis() {
        return this;
    }

    @Override
    public ExitBuilder resetEntity() {
        entity = factory.getExitInstance();
        return this;
    }

    public Exit buildExit() {
        return entity;
    }
}
