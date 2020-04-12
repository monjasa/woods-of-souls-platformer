package org.monjasa.engine.menu;

import javafx.scene.layout.VBox;

public class PlatformerMenuBox extends VBox {

    public PlatformerMenuBox(PlatformerMenuButton... menuButtons) {

        for (PlatformerMenuButton button : menuButtons) {
            add(button);
        }
    }

    public void add(PlatformerMenuButton button) {
        button.setParent(this);
        getChildren().add(button);
    }
}
