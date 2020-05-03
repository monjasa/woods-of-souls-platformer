package org.monjasa.engine.ui;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.monjasa.engine.entities.components.EntityHPComponent;

public class HealthBarUI extends StackPane {

    private static final int WIDTH = 340;
    private static final int HEIGHT = 45;

    private EntityHPComponent playerHP;

    private Rectangle bar;
    private Text currentHealthLabel;

    public HealthBarUI(EntityHPComponent playerHP) {

        this.playerHP = playerHP;

        Rectangle background = new Rectangle(340, 45, Color.rgb(40, 40, 40, 0.90));
        background.setTranslateX(40);

        bar = new Rectangle(WIDTH, HEIGHT, Color.rgb(237, 21, 29));
        bar.setTranslateX(40);

        currentHealthLabel = new Text();
        currentHealthLabel.fontProperty().setValue(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(36));
        currentHealthLabel.fillProperty().setValue(Color.WHITE);

        currentHealthLabel.textProperty().bind(Bindings.concat(
                playerHP.valueProperty().asString(),
                " / ",
                playerHP.maxValueProperty().asString()
        ));

        currentHealthLabel.setTranslateX(40 + WIDTH / 2.0 - currentHealthLabel.getLayoutBounds().getWidth() / 2.0);

        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(
                background,
                bar,
                currentHealthLabel,
                FXGL.texture("health-bar.png")
        );

        bindHPChangeListeners();
    }

    private void bindHPChangeListeners() {

        playerHP.maxValueProperty().addListener((observable, oldValue, newMaxHP) -> {
            hpChanged(playerHP.getValue());
        });

        playerHP.valueProperty().addListener((observable, oldValue, newHP) -> {
            hpChanged(newHP.intValue());
        });
    }

    public void updatePlayerHP(EntityHPComponent playerHP) {

        this.playerHP = playerHP;

        bindHPChangeListeners();

        currentHealthLabel.textProperty().bind(Bindings.concat(
                playerHP.valueProperty().asString(),
                " / ",
                playerHP.maxValueProperty().asString()
        ));

        hpChanged(playerHP.getValue());
    }

    private void hpChanged(int hp) {

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(
                bar.widthProperty(),
                hp * 1.0 / playerHP.getMaxValue() * WIDTH, Interpolators.LINEAR.EASE_IN_OUT()
        );

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(750), keyValue));
        timeline.play();
    }
}
