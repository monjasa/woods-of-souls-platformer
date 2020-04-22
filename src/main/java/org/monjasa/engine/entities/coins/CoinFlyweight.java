package org.monjasa.engine.entities.coins;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class CoinFlyweight {

    private AnimationChannel channel;
    private Sound collectSound;

    CoinFlyweight(String spriteSheet, String collectSoundName) {

        this.channel = new AnimationChannel(FXGL.image(spriteSheet),
                4, 80, 80, Duration.millis(1800), 0, 11);

        this.collectSound = FXGL.getAssetLoader().loadSound(collectSoundName);
    }

    void playCollectSound() {
        FXGL.getAudioPlayer().playSound(collectSound);
    }

    AnimationChannel getChannel() {
        return channel;
    }
}
