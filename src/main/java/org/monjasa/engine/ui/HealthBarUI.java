package org.monjasa.engine.ui;

import com.almasb.fxgl.animation.Interpolators;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.monjasa.engine.entities.players.components.PlayerHPComponent;

public class HealthBarUI extends StackPane {

    private static final int WIDTH = 340;
    private static final int HEIGHT = 45;

    private PlayerHPComponent playerHP;

    private Rectangle bar;

    public HealthBarUI(PlayerHPComponent playerHP) {
        Color colorRed = Color.rgb(255, 22, 29);

        this.playerHP = playerHP;

        bar = new Rectangle(WIDTH, HEIGHT, colorRed);

        playerHP.valueProperty().addListener((o, old, hp) -> {
            hpChanged(hp.intValue());
        });

        getChildren().add(bar);
        setAlignment(Pos.CENTER_LEFT);
    }

    public void updatePlayerHP(PlayerHPComponent playerHP) {

        this.playerHP = playerHP;

        playerHP.valueProperty().addListener((o, old, hp) -> {
            hpChanged(hp.intValue());
        });

        hpChanged(playerHP.getValue());
    }

    private void hpChanged(int hp) {
        var timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(0.66),
                        new KeyValue(bar.widthProperty(),
                                hp * 1.0 / playerHP.getMaxHP() * WIDTH, Interpolators.LINEAR.EASE_IN_OUT()))
        );
        timeline.play();
    }
}
