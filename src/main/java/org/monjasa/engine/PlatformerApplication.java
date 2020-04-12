package org.monjasa.engine;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.entity.SpawnData;
import org.monjasa.engine.entities.factories.ForestEntityFactory;
import org.monjasa.engine.entities.factories.PlatformerEntityFactory;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.players.Player;
import org.jetbrains.annotations.NotNull;
import org.monjasa.engine.menu.PlatformerMainMenu;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlatformerApplication extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.1.5");

        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(false);
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return new PlatformerMainMenu(MenuType.MAIN_MENU);
            }
        });
    }

    @Override
    protected void initGame() {

        PlatformerEntityFactory entityFactory = new ForestEntityFactory();

        Player player = entityFactory.createPlayer(new SpawnData(100, 50));

        List<Platform> platforms = List.of(
                entityFactory.createPlatform(new SpawnData(50, 400)),
                entityFactory.createPlatform(new SpawnData(350, 550)),
                entityFactory.createPlatform(new SpawnData(500, 150))
        );

        getGameWorld().addEntity(player);
        platforms.forEach(getGameWorld()::addEntity);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
