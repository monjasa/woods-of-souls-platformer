package org.monjasa.engine.scenes.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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

        getStyleClass().add("platformer-menu-button-wrapper");

        button = new Button(stringKey);
        button.getStyleClass().add("platformer-menu-button");
        button.setFont(getAssetLoader().loadFont("gnomoria.ttf").newFont(48));

        Rectangle background = new Rectangle(250, 60);
        background.getStyleClass().add("platformer-menu-button-background");
        background.setEffect(new GaussianBlur(5));

        setMargin(button, new Insets(0, 0, 0, 15));
        getChildren().addAll(background, button);

        DropShadow dropShadow = new DropShadow(50, Color.WHITE);
        dropShadow.setInput(new Glow());

        button.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> setEffect(dropShadow));
        button.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> setEffect(null));
        button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> setEffect(null));
    }

    public void setParent(PlatformerMenuBox parent) {
        this.parent = parent;
    }

    public void setOnAction(EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);
    }
}
