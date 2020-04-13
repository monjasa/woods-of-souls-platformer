package org.monjasa.engine.entities.players;

import org.monjasa.engine.entities.PlatformerEntityBuilder;
import org.monjasa.engine.entities.factories.PlatformerEntityFactory;

public class PlayerBuilder extends PlatformerEntityBuilder<PlayerBuilder, Player> {

    public PlayerBuilder(PlatformerEntityFactory factory) {
        super(factory);
        entity = factory.getPlayerInstance();
    }

    @Override
    protected PlayerBuilder getThis() {
        return this;
    }

    @Override
    public PlayerBuilder resetEntity() {
        entity = factory.getPlayerInstance();
        return this;
    }

    public Player buildPlayer() {
        return entity;
    }
}
