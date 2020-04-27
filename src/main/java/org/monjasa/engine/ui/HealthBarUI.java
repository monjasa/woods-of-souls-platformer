package org.monjasa.engine.ui;

import com.almasb.fxgl.animation.Interpolators;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.monjasa.engine.entities.players.components.PlayerHPComponent;

public class HealthBarUI extends StackPane {
    private static final int RADIUS = 45;

    private PlayerHPComponent playerHP;

    private Circle inner;

    public HealthBarUI(PlayerHPComponent playerHP) {
        this.playerHP = playerHP;

        var outer = new Circle(RADIUS, RADIUS, RADIUS, null);
        outer.setStroke(Color.GREEN);
        outer.setStrokeWidth(6.0);

        outer.strokeProperty().bind(
                Bindings.when(playerHP.valueProperty().divide(playerHP.getMaxHP() * 1.0).greaterThan(0.25)).then(Color.GREEN).otherwise(Color.RED)
        );

        inner = new Circle(RADIUS - 2, RADIUS - 2, RADIUS - 2, Color.LIGHTGREEN.brighter());

        inner.fillProperty().bind(
                Bindings.when(playerHP.valueProperty().divide(playerHP.getMaxHP() * 1.0).greaterThan(0.25)).then(Color.LIGHTGREEN.brighter()).otherwise(Color.RED.brighter())
        );

        playerHP.valueProperty().addListener((o, old, hp) -> {
            hpChanged(hp.intValue());
        });

        getChildren().addAll(inner, outer);
    }

    private void hpChanged(int hp) {
        var timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(0.66), new KeyValue(inner.radiusProperty(), hp * 1.0 / playerHP.getMaxHP() * RADIUS, Interpolators.LINEAR.EASE_IN_OUT()))
        );
        timeline.play();
    }

    public void updatePlayerHP(PlayerHPComponent playerHP) {

        this.playerHP = playerHP;

        playerHP.valueProperty().addListener((o, old, hp) -> {
            hpChanged(hp.intValue());
        });

        hpChanged(playerHP.getValue());
    }
}
