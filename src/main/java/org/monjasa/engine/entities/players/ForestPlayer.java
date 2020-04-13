package org.monjasa.engine.entities.players;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ForestPlayer extends Player {

    @Override
    public void moveLeft() {
        getComponent(PhysicsComponent.class).setVelocityX(-200);
        getComponent(ForestPlayerViewComponent.class).onMovingHorizontally();
    }

    @Override
    public void moveRight() {
        getComponent(PhysicsComponent.class).setVelocityX(200);
        getComponent(ForestPlayerViewComponent.class).onMovingHorizontally();
    }

    @Override
    public void stopHorizontal() {
        getComponent(PhysicsComponent.class).setVelocityX(0);
    }

    @Override
    public void jump() {
        getComponent(PhysicsComponent.class).setVelocityY(-350);
    }

    public static class ForestPlayerViewComponent extends Component {

        @Override
        public void onAdded() {
            entity.getViewComponent().addChild(FXGL.texture("player.png"));
        }

        public void onMovingHorizontally() {
            entity.setScaleX(Math.signum(entity.getComponent(PhysicsComponent.class).getVelocityX()));
        }
    }
}
