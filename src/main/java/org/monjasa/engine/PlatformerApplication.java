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
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveFile;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.monjasa.engine.entities.PlatformerEntityFactory;
import org.monjasa.engine.entities.PlatformerEntityType;
import org.monjasa.engine.entities.components.EntityHPComponent;
import org.monjasa.engine.entities.enemies.Enemy;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.levels.*;
import org.monjasa.engine.levels.iterator.Collection;
import org.monjasa.engine.levels.iterator.LevelCollection;
import org.monjasa.engine.levels.iterator.LevelIterator;
import org.monjasa.engine.observer.Observer;
import org.monjasa.engine.observer.Subject;
import org.monjasa.engine.perks.PerkTree;
import org.monjasa.engine.scenes.PerkTreeScene;
import org.monjasa.engine.scenes.PlatformerLoadingScene;
import org.monjasa.engine.scenes.menu.PlatformerGameMenu;
import org.monjasa.engine.scenes.menu.PlatformerMainMenu;
import org.monjasa.engine.ui.CoinsUI;
import org.monjasa.engine.ui.HealthBarUI;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;
import static org.monjasa.engine.entities.PlatformerEntityType.*;
import static org.monjasa.engine.levels.PlatformerLevel.LevelMemento;

public class PlatformerApplication extends GameApplication implements Subject {

    private static final boolean DEVELOPING_NEW_LEVEL = true;

    private boolean loadingFromSave = false;

    private PlatformerLevel currentLevel;
    private LevelMemento levelSnapshot;

    private PlatformerEntityFactory entityFactories;

    private HealthBarUI healthBar;

    private PerkTree perkTree;

    private Music mainMenuMusic;
    private Music gameMusic;
    private ImageCursor imageCursor;

    private LevelIterator levelIterator;

    private List<Observer> observers;

    @Override
    protected void initSettings(GameSettings settings) {

        settings.setApplicationMode(ApplicationMode.DEVELOPER);

        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.3.2");

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

        getSaveLoadService().addHandler(new LevelSaveLoadHandler());
    }

    @Override
    protected void initGame() {

        this.entityFactories = new PlatformerFactoryAdapter();

        getGameWorld().addEntityFactory(this.entityFactories);

        Collection levelURLs = new LevelCollection(loadLevelURLs(), entityFactories, DEVELOPING_NEW_LEVEL);
        levelIterator = levelURLs.createConsistentLevelIterator();

        observers = new ArrayList<>();

        getPhysicsWorld().setGravity(0, 1000);

        getGameScene().setCursor(imageCursor.getImage(), new Point2D(0, 0));
        getGameScene().getContentRoot().getChildren().forEach(node -> node.setCursor(Cursor.NONE));

        getAudioPlayer().stopMusic(mainMenuMusic);
        getAudioPlayer().loopMusic(gameMusic);

        perkTree = new PerkTree();
        prepareNextLevel();
    }

    public void startGame() {

        if (loadingFromSave) {
            SaveFile saveFile = getSaveLoadService().readSaveFileTask(new SaveFile(
                    "progress",
                    "player",
                    "ser"
            )).run();

            getSaveLoadService().load(saveFile.getData());
            notifyObservers();
            for (int i = 1; i < geti("level"); i++) prepareLevel();
            prepareLevel();

            loadingFromSave = false;
        }

        set("initialLevel", false);
        getGameController().gotoPlay();
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("level", 0);
        vars.put("initialLevel", true);
        vars.put("coinsCollected", 0);
        vars.put("coinsAvailable", 0);
    }

    @Override
    protected void initInput() {

        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                FXGL.<PlatformerApplication>getAppCast().<Player>getSingletonCast(PLAYER).goLeft();
            }

            @Override
            protected void onActionEnd() {
                FXGL.<PlatformerApplication>getAppCast().<Player>getSingletonCast(PLAYER).horizontalStop();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                FXGL.<PlatformerApplication>getAppCast().<Player>getSingletonCast(PLAYER).goRight();
            }

            @Override
            protected void onActionEnd() {
                FXGL.<PlatformerApplication>getAppCast().<Player>getSingletonCast(PLAYER).horizontalStop();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                FXGL.<PlatformerApplication>getAppCast().<Player>getSingletonCast(PLAYER).goUp();
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Attack") {
            @Override
            protected void onActionBegin() {
                FXGL.<PlatformerApplication>getAppCast().<Player>getSingletonCast(PLAYER).attack();
            }
        }, KeyCode.X);

        getInput().addAction(new UserAction("Open Perk Tree") {
            @Override
            protected void onAction() {
                getSceneService().pushSubScene(new PerkTreeScene());
            }
        }, KeyCode.F);

    }

    @Override
    protected void initUI() {

        CoinsUI coinsPlate = new CoinsUI();
        registerObserver(coinsPlate);

        healthBar = new HealthBarUI(
                getGameWorld().getSingleton(PLAYER).getComponent(EntityHPComponent.class)
        );

        addUINode(coinsPlate, 30, 100);
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
                getWorldProperties().increment("coinsCollected", 1);
                changeCoinsAvailableValue(1);

                entityFactories.getCurrentFactory().getCoinInstance().onCollected();

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

                checkpoint.removeComponent(CollidableComponent.class);

                Text checkpointReachedText = new Text("You reached the checkpoint!");
                checkpointReachedText.fontProperty().setValue(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(42));
                checkpointReachedText.fillProperty().setValue(Color.WHITE);
                checkpointReachedText.setTranslateX(10);
                checkpointReachedText.setTranslateY(getAppHeight() - 10);

                addUINode(checkpointReachedText);

                runOnce(() -> {
                    removeUINode(checkpointReachedText);
                }, Duration.millis(3000));
            }
        });
    }

    public void onPlayerDied() {
        getDialogService().showMessageBox("You died", this::restartFromSnapshot);
    }

    private void restartFromSnapshot() {
        currentLevel.restoreLevel(levelSnapshot);
    }

    private List<URL> loadLevelURLs() {

        List<URL> levelURLs = new ArrayList<>();

        entityFactories.getLevelFactories().forEach(factory -> {
            for (int i = 0; i < factory.getMaxLevel(); i++)
                levelURLs.add(getClass().getClassLoader().getResource(
                        String.format("assets/levels/tmx/%s_%02d.tmx", factory.getLevelPrefix(), i))
                );
        });

        return levelURLs;
    }

    private PlatformerLevel prepareLevel() {

        currentLevel = new PlatformerLevel(levelIterator.getNext());
        levelSnapshot = currentLevel.makeSnapshot();

        getGameWorld().setLevel(currentLevel.getLevel());

        Entity player = getGameWorld().getSingleton(PLAYER);

        getGameScene().getViewport().setLazy(true);
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        getGameScene().getViewport().setBounds(0, 0, currentLevel.getLevel().getWidth(),
                currentLevel.getLevel().getHeight());

        if (!getb("initialLevel")) {

            healthBar.updatePlayerHP(player.getComponent(EntityHPComponent.class));
            FXGL.getExecutor().startAsyncFX(() -> {

                String savingGameMessage = "Saving game";
                Text savingGameTitle = new Text();
                savingGameTitle.fontProperty().setValue(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(42));
                savingGameTitle.fillProperty().setValue(Color.WHITE);
                savingGameTitle.setTranslateX(10);
                savingGameTitle.setTranslateY(getAppHeight() - 10);

                StringProperty savingGameProcessMessage = new SimpleStringProperty("");
                run(() -> savingGameProcessMessage.setValue(savingGameProcessMessage.concat(".").getValue()), Duration.millis(500), 3);
                savingGameTitle.textProperty().bind(Bindings.concat(savingGameMessage, savingGameProcessMessage));

                Text gameSavedTitle = new Text("Game saved!");
                gameSavedTitle.fontProperty().setValue(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(42));
                gameSavedTitle.fillProperty().setValue(Color.WHITE);
                gameSavedTitle.setTranslateX(10);
                gameSavedTitle.setTranslateY(getAppHeight() - 10);

                addUINode(savingGameTitle);
                saveGame();

                runOnce(() -> {
                    removeUINode(savingGameTitle);
                    addUINode(gameSavedTitle);

                    runOnce(() -> {
                        removeUINode(gameSavedTitle);
                    }, Duration.millis(2000));
                }, Duration.millis(2000));
            });
        }

        return currentLevel;
    }

    @SuppressWarnings("UnusedReturnValue")
    private Optional<PlatformerLevel> prepareNextLevel() {

        if (!levelIterator.hasNext()) {
            getDialogService().showMessageBox("The end of Alpha version.\nThank you for playing!",
                    getGameController()::gotoMainMenu);
            return Optional.empty();
        }

        return Optional.of(prepareLevel());
    }

    private void finishLevel() {

        if (!DEVELOPING_NEW_LEVEL) {
            inc("level", 1);
        }

        getGameScene().getViewport().fade(this::prepareNextLevel);
    }

    private void saveGame() {

        perkTree.savePerkTree();

        DataFile dataFile = new DataFile();
        getSaveLoadService().save(dataFile);
        getSaveLoadService().writeSaveFileTask(new SaveFile(
                "progress",
                "player",
                "ser",
                LocalDateTime.now(),
                dataFile
        )).run();
    }

    public void changeCoinsAvailableValue(int difference) {
        inc("coinsAvailable", difference);
        notifyObservers();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        if (observers.indexOf(o) >= 0) {
            observers.remove(o);
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(getWorldProperties().getInt("coinsAvailable"));
        }
    }

    public void setLoadingFromSaveState() {
        loadingFromSave = true;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T getSingletonCast(PlatformerEntityType entityType) {
        return (T) getGameWorld().getSingleton(entityType);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public PerkTree getPerkTree() {
        return perkTree;
    }

    public LevelMemento getLevelSnapshot() {
        return levelSnapshot;
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
