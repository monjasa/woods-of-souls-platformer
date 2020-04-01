package org.monjasa.engine.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PlayerControlComponent extends Component {

    private static final int PLAYER_MOVE_SPEED = 150;
    private static final int PLAYER_JUMP_SPEED = 350;

    private PhysicsComponent physicComponent;

    public void moveLeft() {
        physicComponent.setVelocityX(-PLAYER_MOVE_SPEED);
        entity.setScaleX(-1);
    }

    public void moveRight() {
        physicComponent.setVelocityX(PLAYER_MOVE_SPEED);
        entity.setScaleX(1);
    }

    public void jump() {
        physicComponent.setVelocityY(-PLAYER_JUMP_SPEED);
    }

    public void stopHorizontalMoving() {
        physicComponent.setVelocityX(0);
    }
}
