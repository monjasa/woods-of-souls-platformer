package org.monjasa.engine.entities.enemies;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class ForestEnemy extends Enemy {

    public ForestEnemy() {

    }

    public static class ForestEnemyComponent extends Component {

        private AnimatedTexture texture;

        private AnimationChannel animationWalk;

        private int patrolEndX;
        private boolean movingRight = false;

        private LocalTimer timer;
        private Duration duration;
        private double distance;
        private double speed;

        public ForestEnemyComponent(int patrolEndX) {
            this.patrolEndX = patrolEndX;

            animationWalk = new AnimationChannel(FXGL.image("enemy-spritesheet.png"), 4,
                    135, 135, Duration.millis(1000), 0, 5);

            texture = new AnimatedTexture(animationWalk).loop();

            duration = Duration.seconds(2);
        }

        @Override
        public void onAdded() {
            distance = patrolEndX - entity.getX();

            timer = FXGL.newLocalTimer();
            timer.capture();
            speed = distance / duration.toSeconds();

            entity.getTransformComponent().setScaleOrigin(new Point2D(37.5, 49.5));
            entity.getViewComponent().addChild(texture);
        }

        @Override
        public void onUpdate(double tpf) {
            if (timer.elapsed(duration)) {
                movingRight = !movingRight;
                timer.capture();
            }

            entity.translateX(movingRight ? -speed * tpf : speed * tpf);
            entity.setScaleX(movingRight ? -1 : 1);
        }
    }
}
