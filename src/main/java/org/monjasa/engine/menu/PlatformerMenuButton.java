package org.monjasa.engine.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;

public class PlatformerMenuButton extends StackPane {

    private PlatformerMenuBox parent;
    private Button button;

    public PlatformerMenuButton(String stringKey) {

        button = new Button(stringKey);
        button.setFont(getAssetLoader().loadFont("gnomoria.ttf").newFont(48));
        button.setPrefSize(350, 40);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle("-fx-background-color: transparent");
        button.setTextFill(Color.WHITE);

        Rectangle background = new Rectangle(250, 60);
        background.setFill(Color.BLACK);
        background.setOpacity(0.6);
        background.setEffect(new GaussianBlur(5));

        setAlignment(Pos.CENTER_LEFT);
        setRotate(-3);
        getChildren().addAll(background, button);

        setMargin(button, new Insets(0, 0, 0, 15));

        DropShadow dropShadow = new DropShadow(50, Color.WHITE);
        dropShadow.setInput(new Glow());

        setOnMouseEntered(event -> {
            background.setTranslateX(10);
            button.setTranslateX(10);
            background.setFill(Color.WHITE);
            button.setTextFill(Color.BLACK);
        });

        setOnMouseExited(event -> {
            background.setTranslateX(0);
            button.setTranslateX(0);
            background.setFill(Color.BLACK);
            button.setTextFill(Color.WHITE);
        });

        button.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> setEffect(dropShadow));
        button.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> setEffect(null));
    }

    public void setParent(PlatformerMenuBox parent) {
        this.parent = parent;
    }

    public void setOnAction(EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);
    }
}
