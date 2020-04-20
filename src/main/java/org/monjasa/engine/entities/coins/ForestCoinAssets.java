package org.monjasa.engine.entities.coins;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class ForestCoinAssets {

    private AnimationChannel animation;
    private Sound pickUpSound;

    public ForestCoinAssets() {

        this.animation = new AnimationChannel(FXGL.image("forest-coin-spritesheet.png"),
                6, 40, 40, Duration.millis(500), 0, 5);
        this.pickUpSound = FXGL.getAssetLoader().loadSound("pickup-coin.wav");
    }

    public void playPickUpSound() {
         FXGL.getAudioPlayer().playSound(pickUpSound);
    }

    public AnimationChannel getTexture() {
        return animation;
    }
}
