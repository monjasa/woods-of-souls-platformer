package org.monjasa.engine.entities.players.components;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import org.monjasa.engine.entities.players.ForestPlayer;

import java.util.List;

public class ForestPlayerViewComponent extends PlayerViewComponent {

    private ForestPlayer parentEntity;

    private PhysicsComponent physicsComponent;

    private Music walkingSounds;

    private AnimatedTexture animatedTexture;

    private List<AnimationChannel> uninterruptibleAnimations;

    private AnimationChannel animationIdle;
    private AnimationChannel animationWalking;
    private AnimationChannel animationBeforeJump;
    private AnimationChannel animationJumping;
    private AnimationChannel animationAfterJump;

    private AnimationChannel animationDaggerAttack;
    private AnimationChannel animationBowAttack;

    public ForestPlayerViewComponent() {

        walkingSounds = FXGL.getAssetLoader().loadMusic("walking-sound.mp3");

        animationIdle = new AnimationChannel(FXGL.image("player-spritesheet.png"), 4,
                90, 165, Duration.INDEFINITE, 0, 0);

        animationWalking = new AnimationChannel(FXGL.image("player-spritesheet.png"), 4,
                90, 165, Duration.millis(700), 1, 2);

        animationBeforeJump = new AnimationChannel(FXGL.image("player-spritesheet.png"), 4,
                90, 165, Duration.INDEFINITE, 3, 3);

        animationJumping = new AnimationChannel(FXGL.image("player-spritesheet.png"), 4,
                90, 165, Duration.millis(700), 4, 4);

        animationAfterJump = new AnimationChannel(FXGL.image("player-spritesheet.png"), 4,
                90, 165, Duration.INDEFINITE, 5, 5);

        animationDaggerAttack = new AnimationChannel(FXGL.image("player-spritesheet.png"), 4,
                90, 165, Duration.INDEFINITE, 6, 6);

        animationBowAttack = new AnimationChannel(FXGL.image("player-spritesheet.png"), 4,
                90, 165, Duration.INDEFINITE, 7, 7);

        uninterruptibleAnimations = List.of(
                animationAfterJump,
                animationDaggerAttack,
                animationBowAttack
        );

        animatedTexture = new AnimatedTexture(animationIdle).loop();
    }

    @Override
    public void onAdded() {
        parentEntity = (ForestPlayer) entity;
        parentEntity.getViewComponent().addChild(animatedTexture);
    }

    @Override
    public void onMovingHorizontally() {

        parentEntity.setScaleX(Math.signum(physicsComponent.getVelocityX()));

        if (!parentEntity.getPlayerControlComponent().isMovingVertically()) {
            FXGL.getAudioPlayer().loopMusic(walkingSounds);
            AnimationChannel currentAnimation = animatedTexture.getAnimationChannel();
            if (currentAnimation != animationWalking && !uninterruptibleAnimations.contains(currentAnimation))
                animatedTexture.loopAnimationChannel(animationWalking);
        }
    }

    @Override
    public void onHorizontalStop() {
        FXGL.getAudioPlayer().stopMusic(walkingSounds);
        if (animatedTexture.getAnimationChannel() != animationIdle)
            animatedTexture.loopAnimationChannel(animationIdle);
    }

    @Override
    public void onVerticalStart() {
    }

    @Override
    public void onMovingVertically() {
        FXGL.getAudioPlayer().stopMusic(walkingSounds);
        if (animatedTexture.getAnimationChannel() != animationJumping && animatedTexture.getAnimationChannel() != animationBeforeJump)
            animatedTexture.loopAnimationChannel(animationJumping);
    }

    @Override
    public void onVerticalStop() {
        FXGL.play("landing-sound.wav");
        animatedTexture.playAnimationChannel(animationAfterJump);
        FXGL.runOnce(() -> animatedTexture.loopAnimationChannel(animationIdle), Duration.millis(300));
    }

    @Override
    public void onMeleeAttack() {
        animatedTexture.playAnimationChannel(animationDaggerAttack);
        FXGL.runOnce(() -> animatedTexture.loopAnimationChannel(animationIdle), Duration.millis(500));
    }

    @Override
    public void onRangedAttack() {
        animatedTexture.playAnimationChannel(animationBowAttack);
        FXGL.runOnce(() -> animatedTexture.loopAnimationChannel(animationIdle), Duration.millis(500));
    }
}
