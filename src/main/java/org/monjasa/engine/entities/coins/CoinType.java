package org.monjasa.engine.entities.coins;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class CoinType {

    private AnimationChannel channel;
    private Sound pickUpSound;

    public CoinType(String spriteSheet, String pickUpSound) {

        this.channel = new AnimationChannel(FXGL.image(spriteSheet),
                6, 40, 40, Duration.millis(500), 0, 5);

        this.pickUpSound = FXGL.getAssetLoader().loadSound(pickUpSound);
    }

    public void playPickUpSound() {
        FXGL.getAudioPlayer().playSound(pickUpSound);
    }

    public AnimationChannel getChannel() {
        return channel;
    }
}
