package org.monjasa.engine.entities.players;

import org.monjasa.engine.entities.builder.EntityBuilder;
import org.monjasa.engine.entities.builder.PlatformerEntityBuilder;
import org.monjasa.engine.entities.factories.PlatformerEntityFactory;

public class PlayerBuilder extends PlatformerEntityBuilder {

    public PlayerBuilder(PlatformerEntityFactory factory) {
        super(factory);
        entity = factory.getPlayerInstance();
    }

    @Override
    public EntityBuilder resetEntity() {
        entity = factory.getPlayerInstance();
        return this;
    }
}
