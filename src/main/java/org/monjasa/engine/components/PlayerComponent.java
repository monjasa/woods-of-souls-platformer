package org.monjasa.engine.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.Texture;

public class PlayerComponent extends Component {

    private static final int PLAYER_MOVE_SPEED = 200;
    private static final int PLAYER_JUMP_SPEED = 400;

    private PhysicsComponent physicsComponent;
    private Texture playerTexture;

    public PlayerComponent() {
        playerTexture = FXGL.texture("player.png");
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(playerTexture);
    }

    public void moveLeft() {
        physicsComponent.setVelocityX(-PLAYER_MOVE_SPEED);
        entity.setScaleX(-1);
    }

    public void moveRight() {
        physicsComponent.setVelocityX(PLAYER_MOVE_SPEED);
        entity.setScaleX(1);
    }

    public void horizontalStop() {
        physicsComponent.setVelocityX(0);
    }

    public void jump() {
        physicsComponent.setVelocityY(-PLAYER_JUMP_SPEED);
    }
}
