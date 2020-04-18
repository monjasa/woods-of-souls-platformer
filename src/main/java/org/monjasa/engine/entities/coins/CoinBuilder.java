package org.monjasa.engine.entities.coins;

import org.monjasa.engine.entities.PlatformerEntityBuilder;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;

public class CoinBuilder extends PlatformerEntityBuilder<CoinBuilder, Coin> {

    public CoinBuilder(PlatformerLevelFactory factory) {
        super(factory);
        entity = factory.getCoinInstance();
    }

    @Override
    protected CoinBuilder getThis() {
        return this;
    }

    @Override
    public CoinBuilder resetEntity() {
        entity = factory.getCoinInstance();
        return this;
    }

    public Coin buildCoin() {
        return entity;
    }
}
