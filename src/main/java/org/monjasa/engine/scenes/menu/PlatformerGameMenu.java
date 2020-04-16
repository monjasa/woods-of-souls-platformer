package org.monjasa.engine.scenes.menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.Scene;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.monjasa.engine.PlatformerApplication;

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
//        itemOptions.setOnAction(actionEvent -> fireNewGame());
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
    public void onCreate() {
        getContentRoot().setCursor(((PlatformerApplication) FXGL.getApp()).getImageCursor());
    }

    @Override
    public void onEnteredFrom(@NotNull Scene prevState) {
        if (prevState instanceof GameScene)
            FXGL.getGameScene().getContentRoot().setEffect(new GaussianBlur(5));
    }

    @Override
    public void onExitingTo(@NotNull Scene nextState) {
        if (nextState instanceof SubScene)
            nextState.getContentRoot().setCursor(getContentRoot().getCursor());
        else if (nextState instanceof GameScene)
            FXGL.getGameScene().getContentRoot().setEffect(null);
    }

    @NotNull
    @Override
    protected Node createBackground(double width, double height) {
        return new Rectangle(width, height, Color.rgb(31, 31, 31, 0.50));
    }

    @NotNull
    @Override
    protected Node createTitleView(@NotNull String s) {

        Texture logo = FXGL.texture("logo-texture.png", 400, 272);

        StackPane titleRoot = new StackPane();
        titleRoot.getChildren().addAll(logo);
        titleRoot.setTranslateX(getAppWidth() / 2.0 - logo.getLayoutBounds().getWidth() / 2);
        titleRoot.setTranslateY(20.0);

        return titleRoot;
    }

    @NotNull
    @Override
    protected Node createVersionView(@NotNull String version) {

        Text versionView = new Text(version);
        versionView.setFont(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(18));
        versionView.setFill(Color.WHITE);
        versionView.setTranslateX(5);
        versionView.setTranslateY(FXGL.getAppHeight() - 5);

        return versionView;
    }

    @NotNull
    @Override
    protected Button createActionButton(@NotNull StringBinding stringBinding, @NotNull Runnable runnable) {
        return new Button(stringBinding.get());
    }

    @NotNull
    @Override
    protected Button createActionButton(@NotNull String name, @NotNull Runnable runnable) {
        return new Button(name);
    }

    @NotNull
    @Override
    protected Node createProfileView(@NotNull String profileName) {
        return new Text(profileName);
    }
}
