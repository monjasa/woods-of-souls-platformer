package org.monjasa.engine.entities.coins;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

public class ForestCoin extends Coin {

    private CoinFlyweight coinFlyweight;

    public ForestCoin(CoinFlyweight coinFlyweight) {
        this.coinFlyweight = coinFlyweight;
    }

    @Override
    public void onCollected() {
        coinFlyweight.playCollectSound();
    }

    private AnimationChannel getAnimatedChannel() {
        return coinFlyweight.getChannel();
    }

    public static class ForestCoinViewComponent extends Component {
        @Override
        public void onAdded() {
            AnimatedTexture animatedTexture = new AnimatedTexture(((ForestCoin) entity).getAnimatedChannel());
            animatedTexture.loop();
            entity.getViewComponent().addChild(animatedTexture);
        }
    }
}
