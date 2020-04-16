package org.monjasa.engine;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.ImageCursor;
import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;
import org.monjasa.engine.entities.PlatformerEntityType;
import org.monjasa.engine.entities.factories.ForestLevelFactory;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.scenes.PlatformerLoadingScene;
import org.monjasa.engine.scenes.menu.PlatformerGameMenu;
import org.monjasa.engine.scenes.menu.PlatformerMainMenu;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlatformerApplication extends GameApplication {

    private static final boolean DEVELOPING_NEW_LEVEL = false;

    private Deque<PlatformerLevelFactory> entityFactories;
    private Player player;
    private ImageCursor imageCursor;

    @Override
    protected void initSettings(GameSettings settings) {

        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.1.17");

        List<String> cssRules = new ArrayList<>();
        cssRules.add("styles.css");
        settings.setCSSList(cssRules);

        settings.setFontGame("gnomoria.ttf");
        settings.setFontText("gnomoria.ttf");
        settings.setFontUI("gnomoria.ttf");
        settings.setFontMono("gnomoria.ttf");

        settings.setAppIcon("app/icon.png");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return PlatformerMainMenu.getMainMenuInstance();
            }

            @NotNull
            @Override
            public FXGLMenu newGameMenu() {
                return PlatformerGameMenu.getGameMenuInstance();
            }

            @NotNull
            @Override
            public LoadingScene newLoadingScene() {
                return new PlatformerLoadingScene();
            }
        });
    }

    @Override
    protected void onPreInit() {
        imageCursor = new ImageCursor(FXGL.getAssetLoader().loadCursorImage("cursor.png"));
    }

    @Override
    protected void initGame() {

        entityFactories = new ArrayDeque<>();
        entityFactories.add(new ForestLevelFactory(2));
        entityFactories.forEach(getGameWorld()::addEntityFactory);

        loopBGM("game_background.mp3");
//        getGameScene().setCursor(FXGL.getAssetLoader().loadCursorImage("cursor.png"), new Point2D(0, 0));

        getGameScene().setCursorInvisible();

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
                getDialogService().showMessageBox("The end of Aplha version.\nThank you for playing!", getGameController()::gotoMainMenu);
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

    public ImageCursor getImageCursor() {
        return imageCursor;
    }
}
