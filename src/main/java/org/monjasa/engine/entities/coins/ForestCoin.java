package org.monjasa.engine.entities.coins;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;

public class ForestCoin extends Coin {

    private static ForestCoinAssets assets;

    public ForestCoin(ForestCoinAssets assets) {
        this.assets = assets;
    }

    public static class ForestCoinViewComponent extends Component {

        private AnimatedTexture animatedTexture;

        public ForestCoinViewComponent() {
            animatedTexture = new AnimatedTexture(assets.getTexture()).loop();
        }

        @Override
        public void onAdded() {
            entity.getViewComponent().addChild(animatedTexture);
        }
    }

    @Override
    public void playPickUpSound() {
        assets.playPickUpSound();
    }
}
