package org.monjasa.engine.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

public class PlayerViewComponent extends Component {

    private PhysicsComponent physicsComponent;

    private AnimationChannel animationIdle;
    private AnimationChannel animationWalking;

    private AnimatedTexture playerTexture;

    public PlayerViewComponent() {
        animationIdle = new AnimationChannel(image("player-sprite-sheet.png"), 4,
                32, 42, Duration.INDEFINITE, 1, 1);

        animationWalking = new AnimationChannel(image("player-sprite-sheet.png"), 4,
                32, 42, Duration.millis(500), 0, 3);

        playerTexture = new AnimatedTexture(animationIdle);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(playerTexture);
    }

    @Override
    public void onUpdate(double tpf) {
        if (physicsComponent.getVelocityX() != 0) {
            if (playerTexture.getAnimationChannel() != animationWalking)
                playerTexture.loopAnimationChannel(animationWalking);
        } else {
            if (playerTexture.getAnimationChannel() != animationIdle)
                playerTexture.loopAnimationChannel(animationIdle);
        }
    }
}
