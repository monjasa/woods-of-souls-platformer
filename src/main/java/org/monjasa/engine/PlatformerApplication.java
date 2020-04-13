package org.monjasa.engine;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import org.monjasa.engine.entities.PlatformerEntityType;
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
        settings.setVersion("0.1.7");

        settings.setMainMenuEnabled(false);
        settings.setGameMenuEnabled(false);
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return PlatformerMainMenu.getMainMenuInstance();
            }
        });
    }

    private Player player;

    @Override
    protected void initGame() {

        getGameWorld().addEntityFactory(new ForestEntityFactory());

        setLevelFromMap("tmx/level_00.tmx");

        player = (Player) getGameWorld().getSingleton(PlatformerEntityType.PLAYER);
    }

    @Override
    protected void initInput() {

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.moveLeft();
            }

            @Override
            protected void onActionEnd() {
                player.stopHorizontal();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                player.moveRight();
            }

            @Override
            protected void onActionEnd() {
                player.stopHorizontal();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                player.jump();
            }
        }, KeyCode.UP);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
