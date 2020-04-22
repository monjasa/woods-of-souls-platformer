package org.monjasa.engine;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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

    private static final boolean DEVELOPING_NEW_LEVEL = true;

    private Deque<PlatformerLevelFactory> entityFactories;
    private Player player;

    private Music mainMenuMusic;
    private Music gameMusic;
    private ImageCursor imageCursor;

    @Override
    protected void initSettings(GameSettings settings) {

        settings.setApplicationMode(ApplicationMode.DEVELOPER);

        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.2.7");

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
            @Override
            public FXGLMenu newMainMenu() {
                return PlatformerMainMenu.getMainMenuInstance();
            }

            @Override
            public FXGLMenu newGameMenu() {
                return PlatformerGameMenu.getGameMenuInstance();
            }

            @Override
            public LoadingScene newLoadingScene() {
                return new PlatformerLoadingScene();
            }
        });
    }

    @Override
    protected void onPreInit() {
        gameMusic = FXGL.getAssetLoader().loadMusic("game-background.mp3");
        mainMenuMusic = FXGL.getAssetLoader().loadMusic("main-menu-background.mp3");
        imageCursor = new ImageCursor(FXGL.getAssetLoader().loadCursorImage("cursor.png"));
    }

    @Override
    protected void initGame() {

        entityFactories = new ArrayDeque<>();
        entityFactories.add(new ForestLevelFactory(2));
        entityFactories.forEach(getGameWorld()::addEntityFactory);

        getPhysicsWorld().setGravity(0, 1000);

        getGameScene().setCursor(imageCursor.getImage(), new Point2D(0, 0));
        getGameScene().getContentRoot().getChildren().forEach(node -> node.setCursor(Cursor.NONE));

        getAudioPlayer().stopMusic(mainMenuMusic);
        getAudioPlayer().loopMusic(gameMusic);

        prepareNextLevel();
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("level", 0);
        vars.put("coins-collected", 0);
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
    protected void initUI() {

        Text coinsCollectedText = new Text();
        coinsCollectedText.fontProperty().setValue(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(48));
        coinsCollectedText.textProperty().bind(getWorldProperties().intProperty("coins-collected").asString());

        BorderPane textPane = new BorderPane(coinsCollectedText);
        textPane.setPadding(new Insets(0, 0, 0, 20));

        StackPane coinsPane = new StackPane(texture("ui-border.png"), textPane);
        coinsPane.setTranslateX(20);
        coinsPane.setTranslateY(20);

        FXGL.getGameScene().addUINode(coinsPane);
    }

    @Override
    protected void initPhysics() {

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PlatformerEntityType.PLAYER, PlatformerEntityType.EXIT) {
            @Override
            protected void onCollisionBegin(Entity player, Entity exit) {
                finishLevel();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PlatformerEntityType.PLAYER, PlatformerEntityType.COIN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                getWorldProperties().increment("coins-collected", 1);
                coin.removeFromWorld();
                entityFactories.getFirst().getCoinInstance().onCollected();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PlatformerEntityType.PLAYER, PlatformerEntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity player, Entity enemy) {
                play("game-over.wav");
                getDialogService().showMessageBox("You died", () -> prepareLevel());
            }
        });
    }

    private Level prepareLevel() {

        Level level = Objects.requireNonNull(entityFactories.peek()).createLevel(geti("level"), DEVELOPING_NEW_LEVEL);
        getGameWorld().setLevel(level);

        getWorldProperties().setValue("coins-collected", 0);

        player = (Player) getGameWorld().getSingleton(PlatformerEntityType.PLAYER);

        getGameScene().getViewport().setLazy(true);
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        getGameScene().getViewport().setBounds(0, 0, level.getWidth(), level.getHeight());

        return level;
    }

    private Optional<Level> prepareNextLevel() {

        if (geti("level") == Objects.requireNonNull(entityFactories.peek()).getMaxLevel()) {

            entityFactories.poll();

            if (entityFactories.isEmpty()) {
                getDialogService().showMessageBox("The end of Aplha version.\nThank you for playing!", getGameController()::gotoMainMenu);
                return Optional.empty();
            } else {
                getWorldProperties().setValue("level", 0);
            }
        }

        Level nextLevel = prepareLevel();

        if (!DEVELOPING_NEW_LEVEL) {
            inc("level", 1);
        }

        return Optional.of(nextLevel);
    }

    private void finishLevel() {
        getGameScene().getViewport().fade(this::prepareNextLevel);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Music getMainMenuMusic() {
        return mainMenuMusic;
    }

    public Music getGameMusic() {
        return gameMusic;
    }

    public ImageCursor getImageCursor() {
        return imageCursor;
    }
}
