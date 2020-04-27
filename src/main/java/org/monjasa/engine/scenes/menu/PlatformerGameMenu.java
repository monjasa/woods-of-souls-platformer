package org.monjasa.engine.scenes.menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.Scene;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.monjasa.engine.PlatformerApplication;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;

public class PlatformerGameMenu extends FXGLMenu {

    private static volatile PlatformerGameMenu gameMenu;

    public static PlatformerGameMenu getGameMenuInstance() {

        if (gameMenu == null) {
            synchronized (PlatformerGameMenu.class) {
                if (gameMenu == null) {
                    gameMenu = new PlatformerGameMenu();
                }
            }
        }

        return gameMenu;
    }

    private PlatformerGameMenu() {

        super(MenuType.GAME_MENU);

        PlatformerMenuBox menu = createGameMenuBody();

        double menuX = 150;
        double menuY = getAppHeight() / 3.0;

        getMenuRoot().setTranslateX(menuX);
        getMenuRoot().setTranslateY(menuY);

        getMenuContentRoot().setTranslateX(getAppWidth() - 200);
        getMenuContentRoot().setTranslateY(menuY);

        getMenuRoot().getChildren().add(menu);
        getMenuContentRoot().getChildren().add(new MenuContent());
    }

    private PlatformerMenuBox createGameMenuBody() {

        PlatformerMenuBox platformerMenuBox = new PlatformerMenuBox();

        PlatformerMenuButton itemNewGame = new PlatformerMenuButton("Resume");
        itemNewGame.setOnAction(actionEvent -> fireResume());
        platformerMenuBox.add(itemNewGame);

        PlatformerMenuButton itemOptions = new PlatformerMenuButton("Options");
        platformerMenuBox.add(itemOptions);

        PlatformerMenuButton itemExitToMainMenu = new PlatformerMenuButton("Main Menu");
        itemExitToMainMenu.setOnAction(actionEvent -> fireExitToMainMenu());
        platformerMenuBox.add(itemExitToMainMenu);

        PlatformerMenuButton itemExit = new PlatformerMenuButton("Exit");
        itemExit.setOnAction(actionEvent -> fireExit());
        platformerMenuBox.add(itemExit);

        return platformerMenuBox;
    }

    @Override
    public void onEnteredFrom(Scene prevState) {
        if (prevState instanceof GameScene) {
            FXGL.getAudioPlayer().pauseMusic(FXGL.<PlatformerApplication>getAppCast().getGameMusic());
            getGameScene().getContentRoot().getChildren().forEach(node -> node.setEffect(new GaussianBlur(5)));
        }
    }

    @Override
    public void onExitingTo(Scene nextState) {
        if (nextState instanceof GameScene) {
            FXGL.getAudioPlayer().resumeMusic(FXGL.<PlatformerApplication>getAppCast().getGameMusic());
            getGameScene().getContentRoot().getChildren().forEach(node -> node.setEffect(null));
        }
    }

    @Override
    protected Node createBackground(double width, double height) {
        return new Rectangle(width, height, Color.rgb(31, 31, 31, 0.50));
    }

    @Override
    protected Node createTitleView(String s) {

        Texture logo = FXGL.texture("logo-texture.png", 400, 272);

        StackPane titleRoot = new StackPane();
        titleRoot.getChildren().addAll(logo);
        titleRoot.setTranslateX(getAppWidth() / 2.0 - logo.getLayoutBounds().getWidth() / 2);
        titleRoot.setTranslateY(20.0);

        return titleRoot;
    }

    @Override
    protected Node createVersionView(String version) {

        Text versionView = new Text(version);
        versionView.setFont(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(18));
        versionView.setFill(Color.WHITE);
        versionView.setTranslateX(5);
        versionView.setTranslateY(FXGL.getAppHeight() - 5);

        return versionView;
    }

    @Override
    protected Button createActionButton(StringBinding stringBinding, Runnable runnable) {
        return new Button(stringBinding.get());
    }

    @Override
    protected Button createActionButton(String name, Runnable runnable) {
        return new Button(name);
    }

    @Override
    protected Node createProfileView(String profileName) {
        return new Text(profileName);
    }
}
