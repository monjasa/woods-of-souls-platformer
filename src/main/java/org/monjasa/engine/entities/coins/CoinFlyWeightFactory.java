package org.monjasa.engine.entities.coins;

import java.util.HashMap;
import java.util.Map;

public class CoinFlyWeightFactory {

    private Map<String, CoinType> coinTypes;
    private static CoinFlyWeightFactory instance;

    private CoinFlyWeightFactory() {
        coinTypes = new HashMap<>();
    }

    public static CoinFlyWeightFactory getCoinFactoryInstance() {

        if (instance == null)
            synchronized (CoinFlyWeightFactory.class) {
                if (instance == null)
                    instance = new CoinFlyWeightFactory();
            }

        return instance;
    }

    public CoinType getCoinType(String name, String spriteSheet, String pickUpSound) {

        CoinType result = coinTypes.get(name);

        if (result == null) {
            result = new CoinType(spriteSheet, pickUpSound);
            coinTypes.put(name, result);
        }

        return result;
    }
}
