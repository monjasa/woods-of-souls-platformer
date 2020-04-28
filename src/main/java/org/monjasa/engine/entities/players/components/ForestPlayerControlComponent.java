package org.monjasa.engine.entities.players.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.physics.PhysicsComponent;
import org.monjasa.engine.entities.components.DynamicComponent;
import org.monjasa.engine.entities.players.ForestPlayer;

public class ForestPlayerControlComponent extends PlayerControlComponent {

    private ForestPlayer parentEntity;

    private PhysicsComponent physicsComponent;

    private boolean movingVertically;

    @Override
    public void onAdded() {

        parentEntity = (ForestPlayer) entity;

        physicsComponent.onGroundProperty().addListener((observable, wasOnGround, isOnGround) -> {
            if (!isOnGround && wasOnGround && movingVertically) {
                parentEntity.getPlayerViewComponent().onVerticalStart();
            } else if (isOnGround && !wasOnGround && movingVertically) {
                movingVertically = false;
                parentEntity.getPlayerViewComponent().onVerticalStop();
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {

        if (FXGLMath.abs(physicsComponent.getVelocityY()) > 0) {
            movingVertically = true;
        }

        parentEntity.onUpdate(tpf);
    }

    @Override
    public void moveLeft() {
        physicsComponent.setVelocityX(-1.0 * parentEntity.getComponent(DynamicComponent.class).getHorizontalVelocity());
    }

    @Override
    public void moveRight() {
        physicsComponent.setVelocityX(parentEntity.getComponent(DynamicComponent.class).getHorizontalVelocity());
    }

    @Override
    public void stopHorizontalMoving() {
        physicsComponent.setVelocityX(0);
    }

    @Override
    public void jump() {
        if (physicsComponent.isOnGround()) {
            physicsComponent.setVelocityY(-1.0 * parentEntity.getComponent(DynamicComponent.class).getVerticalVelocity());
        }
    }

    @Override
    public boolean isMovingVertically() {
        return movingVertically;
    }
}
