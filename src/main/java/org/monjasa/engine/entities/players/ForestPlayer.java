package org.monjasa.engine.entities.players;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class ForestPlayer extends Player {

    @Override
    public void goLeft() {
        getComponent(ForestPlayerControlComponent.class).moveLeft();
    }

    @Override
    public void goRight() {
        getComponent(ForestPlayerControlComponent.class).moveRight();
    }

    @Override
    public void horizontalStop() {
        getComponent(ForestPlayerControlComponent.class).stopHorizontalMoving();
    }

    @Override
    public void goUp() {
        getComponent(ForestPlayerControlComponent.class).jump();
    }

    public static class ForestPlayerControlComponent extends Component {

        private PhysicsComponent physicsComponent;
        private ForestPlayerViewComponent viewComponent;

        private boolean movingVertically;

        @Override
        public void onAdded() {
            physicsComponent.onGroundProperty().addListener((observable, wasOnGround, isOnGround) -> {
                if (!isOnGround && wasOnGround && movingVertically) {
                    viewComponent.onVerticalStart();
                } else if (isOnGround && !wasOnGround && movingVertically) {
                    movingVertically = false;
                    viewComponent.onVerticalStop();
                }
            });
        }

        @Override
        public void onUpdate(double tpf) {
            if (FXGLMath.abs(physicsComponent.getVelocityY()) > 0) {
                movingVertically = true;
                viewComponent.onMovingVertically();
            }
        }

        public void moveLeft() {
            physicsComponent.setVelocityX(-200);
            viewComponent.onMovingHorizontally();
        }

        public void moveRight() {
            physicsComponent.setVelocityX(200);
            viewComponent.onMovingHorizontally();
        }

        public void stopHorizontalMoving() {
            physicsComponent.setVelocityX(0);
            viewComponent.onHorizontalStop();
        }

        public void jump() {
            if (physicsComponent.isOnGround()) {
                physicsComponent.setVelocityY(-600);
            }
        }

        public boolean isMovingVertically() {
            return movingVertically;
        }

    }

    public static class ForestPlayerViewComponent extends Component {

        private PhysicsComponent physicsComponent;

        private Music walkingSounds;

        private AnimatedTexture animatedTexture;

        private AnimationChannel animationIdle;
        private AnimationChannel animationWalking;
        private AnimationChannel animationJumping;
        private AnimationChannel animationAfterJump;

        public ForestPlayerViewComponent() {

            walkingSounds = FXGL.getAssetLoader().loadMusic("walking_sound.mp3");

            animationIdle = new AnimationChannel(FXGL.image("player_spritesheet.png"), 4,
                    120, 210, Duration.INDEFINITE, 0, 0);

            animationWalking = new AnimationChannel(FXGL.image("player_spritesheet.png"), 4,
                    120, 210, Duration.millis(750), 1, 2);

            animationJumping = new AnimationChannel(FXGL.image("player_spritesheet.png"), 4,
                    120, 210, Duration.millis(750), 4, 4);

            animationAfterJump = new AnimationChannel(FXGL.image("player_spritesheet.png"), 4,
                    120, 210, Duration.INDEFINITE, 5, 5);

            animatedTexture = new AnimatedTexture(animationIdle).loop();
        }

        @Override
        public void onAdded() {
            entity.getViewComponent().addChild(animatedTexture);
        }

        public void onMovingHorizontally() {
            entity.setScaleX(Math.signum(physicsComponent.getVelocityX()));

            if (!entity.getComponent(ForestPlayerControlComponent.class).isMovingVertically()) {
                FXGL.getAudioPlayer().loopMusic(walkingSounds);
                AnimationChannel currentAnimation = animatedTexture.getAnimationChannel();
                if (currentAnimation != animationWalking && currentAnimation != animationAfterJump)
                    animatedTexture.loopAnimationChannel(animationWalking);
            }
        }

        public void onHorizontalStop() {
            FXGL.getAudioPlayer().stopMusic(walkingSounds);
            if (animatedTexture.getAnimationChannel() != animationIdle)
                animatedTexture.loopAnimationChannel(animationIdle);
        }

        public void onVerticalStart() {
            FXGL.getAudioPlayer().stopMusic(walkingSounds);
        }

        public void onMovingVertically() {
            FXGL.getAudioPlayer().stopMusic(walkingSounds);
            if (animatedTexture.getAnimationChannel() != animationJumping)
                animatedTexture.loopAnimationChannel(animationJumping);
        }

        public void onVerticalStop() {
            FXGL.play("landing-sound.wav");
            animatedTexture.playAnimationChannel(animationAfterJump);
            FXGL.runOnce(() -> animatedTexture.loopAnimationChannel(animationIdle), Duration.millis(300));
        }
    }
}
