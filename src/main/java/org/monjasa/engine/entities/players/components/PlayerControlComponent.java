package org.monjasa.engine.entities.players.components;

import com.almasb.fxgl.entity.component.Component;

public abstract class PlayerControlComponent extends Component {

    public abstract void moveLeft();

    public abstract void moveRight();

    public abstract void stopHorizontalMoving();

    public abstract void jump();

    public abstract boolean isMovingVertically();
}
