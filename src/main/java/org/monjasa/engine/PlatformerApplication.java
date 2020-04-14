package org.monjasa.engine;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;
import org.monjasa.engine.entities.PlatformerEntityType;
import org.monjasa.engine.entities.factories.ForestLevelFactory;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.menu.PlatformerMainMenu;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlatformerApplication extends GameApplication {

    private static final boolean DEVELOPING_NEW_LEVEL = true;

    private Deque<PlatformerLevelFactory> entityFactories;
    private Player player;

    public PlatformerApplication() {
        entityFactories = new ArrayDeque<>();
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.1.11");

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

    @Override
    protected void initGame() {

        entityFactories.add(new ForestLevelFactory(2));

        entityFactories.forEach(getGameWorld()::addEntityFactory);

        prepareNextLevel();
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("level", 0);
    }

    @Override
    protected void initInput() {

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.goLeft();
            }

            @Override
            protected void onActionEnd() {
                player.horizontalStop();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                player.goRight();
            }

            @Override
            protected void onActionEnd() {
                player.horizontalStop();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                player.goUp();
            }
        }, KeyCode.UP);
    }

    @Override
    protected void initPhysics() {

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PlatformerEntityType.PLAYER, PlatformerEntityType.EXIT) {
            @Override
            protected void onCollisionBegin(Entity player, Entity exit) {
                finishLevel();
            }
        });
    }

    private void prepareNextLevel() {

        if (geti("level") == Objects.requireNonNull(entityFactories.peek()).getMaxLevel()) {

            entityFactories.poll();

            if (entityFactories.isEmpty()) {
                getDialogService().showMessageBox("the end", getGameController()::startNewGame);
                return;
            } else {
                getWorldProperties().setValue("level", 0);
            }
        }

        Level level = Objects.requireNonNull(entityFactories.peek()).createLevel(geti("level"), DEVELOPING_NEW_LEVEL);
        getGameWorld().setLevel(level);

        if (!DEVELOPING_NEW_LEVEL) {
            inc("level", 1);
        }

        player = (Player) getGameWorld().getSingleton(PlatformerEntityType.PLAYER);

        getGameScene().getViewport().setLazy(true);
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        getGameScene().getViewport().setBounds(0, 0, level.getWidth(), level.getHeight());
    }

    private void finishLevel() {
        getGameScene().getViewport().fade(this::prepareNextLevel);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
