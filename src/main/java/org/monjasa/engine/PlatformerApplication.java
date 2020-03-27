package org.monjasa.engine;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class PlatformerApplication extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.0.1");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
