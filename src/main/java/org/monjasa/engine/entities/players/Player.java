package org.monjasa.engine.entities.players;

import com.almasb.fxgl.core.Updatable;
import com.almasb.fxgl.entity.Entity;
import org.monjasa.engine.entities.players.components.PlayerControlComponent;
import org.monjasa.engine.entities.players.components.PlayerViewComponent;

public abstract class Player extends Entity implements Updatable {

    PlayerViewComponent playerViewComponent;
    PlayerControlComponent playerControlComponent;

    public abstract Player attachPlayerComponents();

    @Override
    public void onUpdate(double tpf) {
        if (playerControlComponent.isMovingVertically()) playerViewComponent.onMovingVertically();
    }

    public void goLeft() {
        playerControlComponent.moveLeft();
        playerViewComponent.onMovingHorizontally();
    }

    public void goRight() {
        playerControlComponent.moveRight();
        playerViewComponent.onMovingHorizontally();
    }

    public void horizontalStop() {
        playerControlComponent.stopHorizontalMoving();
        playerViewComponent.onHorizontalStop();
    }

    public void goUp() {
        playerControlComponent.jump();
    }

    public PlayerViewComponent getPlayerViewComponent() {
        return playerViewComponent;
    }

    public PlayerControlComponent getPlayerControlComponent() {
        return playerControlComponent;
    }
}
