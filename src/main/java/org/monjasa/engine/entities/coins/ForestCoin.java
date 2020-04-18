package org.monjasa.engine.entities.coins;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class ForestCoin extends Coin {

    public static class ForestCoinViewComponent extends Component {

        private AnimatedTexture animatedTexture;

        public ForestCoinViewComponent() {

            AnimationChannel animation = new AnimationChannel(FXGL.image("forest-coin-spritesheet.png"),
                    6,40, 40, Duration.millis(500), 0, 5);

            animatedTexture = new AnimatedTexture(animation).loop();
        }

        @Override
        public void onAdded() {
            entity.getViewComponent().addChild(animatedTexture);
        }
    }

    @Override
    public void playPickUpCoinSound() {
        FXGL.play("pickup-coin.wav");
    }
}
