package org.monjasa.engine.entities.coins;

import org.monjasa.engine.entities.factories.PlatformerLevelFactory;

import java.util.HashMap;
import java.util.Map;

public class CoinFlyweightFactory {

    private static volatile CoinFlyweightFactory instance;

    public static CoinFlyweightFactory getCoinFactoryInstance() {

        if (instance == null)
            synchronized (CoinFlyweightFactory.class) {
                if (instance == null)
                    instance = new CoinFlyweightFactory();
            }

        return instance;
    }

    private Map<PlatformerLevelFactory, CoinFlyweight> coinFlyweights;

    private CoinFlyweightFactory() {
        coinFlyweights = new HashMap<>();
    }

    public CoinFlyweight getCoinFlyweight(PlatformerLevelFactory factory) {
        return coinFlyweights.computeIfAbsent(
                factory,
                key -> new CoinFlyweight(key.getCoinSpritesheetName(), key.getCoinCollectSoundName())
        );
    }
}
