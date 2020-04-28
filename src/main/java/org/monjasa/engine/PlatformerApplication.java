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
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.monjasa.engine.entities.PlatformerEntityFactory;
import org.monjasa.engine.entities.PlatformerEntityType;
import org.monjasa.engine.entities.components.EntityHPComponent;
import org.monjasa.engine.entities.enemies.Enemy;
import org.monjasa.engine.entities.factories.ForestLevelFactory;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.levels.PlatformerLevel;
import org.monjasa.engine.perks.PerkTree;
import org.monjasa.engine.scenes.PerkTreeScene;
import org.monjasa.engine.scenes.PlatformerLoadingScene;
import org.monjasa.engine.scenes.menu.PlatformerGameMenu;
import org.monjasa.engine.scenes.menu.PlatformerMainMenu;
import org.monjasa.engine.ui.HealthBarUI;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;
import static org.monjasa.engine.entities.PlatformerEntityType.*;
import static org.monjasa.engine.levels.PlatformerLevel.LevelMemento;

public class PlatformerApplication extends GameApplication {

    private static final boolean DEVELOPING_NEW_LEVEL = false;

    private PlatformerLevel currentLevel;
    private LevelMemento levelSnapshot;

    private PlatformerEntityFactory entityFactory;

    private HealthBarUI healthBar;
    private PerkTree perkTree;

    private Music mainMenuMusic;
    private Music gameMusic;
    private ImageCursor imageCursor;

    @Override
    protected void initSettings(GameSettings settings) {

        settings.setApplicationMode(ApplicationMode.DEVELOPER);

        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.2.14");

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

        Deque<PlatformerLevelFactory> entityFactories = new ArrayDeque<>();
        entityFactories.add(new ForestLevelFactory(2));

        entityFactory = new PlatformerFactoryAdapter(entityFactories);

        getGameWorld().addEntityFactory(entityFactory);

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
        vars.put("coins-total-collected", 0);
    }

    @Override
    protected void initInput() {

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                ((Player) getGameWorld().getSingleton(PlatformerEntityType.PLAYER)).goLeft();
            }

            @Override
            protected void onActionEnd() {
                ((Player) getGameWorld().getSingleton(PlatformerEntityType.PLAYER)).horizontalStop();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                ((Player) getGameWorld().getSingleton(PlatformerEntityType.PLAYER)).goRight();
            }

            @Override
            protected void onActionEnd() {
                ((Player) getGameWorld().getSingleton(PlatformerEntityType.PLAYER)).horizontalStop();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                ((Player) getGameWorld().getSingleton(PlatformerEntityType.PLAYER)).goUp();
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Open Perk Tree") {
            @Override
            protected void onAction() {
                getSceneService().pushSubScene(new PerkTreeScene());
            }
        }, KeyCode.F);
    }

    @Override
    protected void initUI() {

        Text coinsCollectedText = new Text();
        coinsCollectedText.fontProperty().setValue(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(36));

        coinsCollectedText.textProperty().bind(getWorldProperties().intProperty("coins-total-collected").asString());

        BorderPane textPane = new BorderPane(coinsCollectedText);
        textPane.setPadding(new Insets(0, 0, 0, 20));

        StackPane coinsPane = new StackPane(texture("ui-border.png"), textPane);
        coinsPane.setTranslateX(30);
        coinsPane.setTranslateY(100);

        healthBar = new HealthBarUI(
                getGameWorld().getSingleton(PlatformerEntityType.PLAYER).getComponent(EntityHPComponent.class)
        );

        addUINode(coinsPane);
        addUINode(healthBar, 20, 30);
    }

    @Override
    protected void initPhysics() {

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PLAYER, EXIT) {
            @Override
            protected void onCollisionBegin(Entity player, Entity exit) {
                finishLevel();
            }

        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PLAYER, COIN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                getWorldProperties().increment("coins-total-collected", 1);
                entityFactory.peekCurrentLevelFactory().getCoinInstance().onCollected();

                currentLevel.addCoinToRestore(coin);

                coin.setVisible(false);
                coin.removeComponent(CollidableComponent.class);
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PLAYER, ENEMY) {
            @Override
            protected void onCollisionBegin(Entity playerEntity, Entity enemyEntity) {
                ((Player) playerEntity).onEnemyHit((Enemy) enemyEntity);
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(PLAYER, CHECKPOINT) {
            @Override
            protected void onCollisionBegin(Entity player, Entity checkpoint) {
                levelSnapshot = currentLevel.onCheckpoint();
            }
        });
    }

    public void onPlayerDied() {
        getDialogService().showMessageBox("You died", this::restartFromSnapshot);
    }

    private void restartFromSnapshot() {

        for (Entity coin : currentLevel.getCoinsToRestore()) {
            coin.addComponent(new CollidableComponent(true));
            coin.setVisible(true);
        }

        currentLevel.restoreLevel(levelSnapshot);
    }

    private PlatformerLevel prepareLevel() {

        currentLevel = new PlatformerLevel(entityFactory, DEVELOPING_NEW_LEVEL);
        levelSnapshot = currentLevel.makeSnapshot();

        getGameWorld().setLevel(currentLevel.getLevel());

        Entity player = getGameWorld().getSingleton(PlatformerEntityType.PLAYER);

        getGameScene().getViewport().setLazy(true);
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        getGameScene().getViewport().setBounds(0, 0, currentLevel.getLevel().getWidth(),
                currentLevel.getLevel().getHeight());

        if (healthBar != null) healthBar.updatePlayerHP(player.getComponent(EntityHPComponent.class));

        perkTree = new PerkTree();

        return currentLevel;
    }

    private Optional<PlatformerLevel> prepareNextLevel() {

        if (geti("level") == entityFactory.peekCurrentLevelFactory().getMaxLevel()) {

            entityFactory.pollCurrentLevelFactory();

            if (entityFactory.isEmpty()) {
                getDialogService().showMessageBox("The end of Alpha version.\nThank you for playing!",
                        getGameController()::gotoMainMenu);
                return Optional.empty();
            } else {
                getWorldProperties().setValue("level", 0);
            }
        }

        return Optional.of(prepareLevel());
    }

    private void finishLevel() {

        if (!DEVELOPING_NEW_LEVEL) {
            inc("level", 1);
        }

        getGameScene().getViewport().fade(this::prepareNextLevel);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public PerkTree getPerkTree() {
        return perkTree;
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
