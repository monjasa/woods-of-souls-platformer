package org.monjasa.engine.entities.coins;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;

public class ForestCoin extends Coin {

    private static CoinType coinType;

    public ForestCoin(CoinType coinType) {
        this.coinType = coinType;
    }

    public static class ForestCoinViewComponent extends Component {

        private AnimatedTexture animatedTexture;

        public ForestCoinViewComponent() {
            animatedTexture = new AnimatedTexture(coinType.getChannel()).loop();
        }

        @Override
        public void onAdded() {
            entity.getViewComponent().addChild(animatedTexture);
        }
    }

    @Override
    public void playPickUpSound() {
        coinType.playPickUpSound();
    }
}
