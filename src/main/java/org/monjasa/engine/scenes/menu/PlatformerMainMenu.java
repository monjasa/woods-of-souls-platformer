package org.monjasa.engine.scenes.menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.Scene;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.StringBinding;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.monjasa.engine.PlatformerApplication;

public class PlatformerMainMenu extends FXGLMenu {

    private static volatile PlatformerMainMenu mainMenu;

    public static PlatformerMainMenu getMainMenuInstance() {

        if (mainMenu == null) {
            synchronized (PlatformerMainMenu.class) {
                if (mainMenu == null) mainMenu = new PlatformerMainMenu();
            }
        }

        return mainMenu;
    }

    private PlatformerMainMenu() {

        super(MenuType.MAIN_MENU);

        PlatformerMenuBox menuBox = createMainMenuBody();

        double menuX = 150;
        double menuY = getAppHeight() / 4.0;

        getMenuRoot().setTranslateX(menuX);
        getMenuRoot().setTranslateY(menuY);

        getMenuContentRoot().setTranslateX(getAppWidth() - 200);

        getMenuRoot().getChildren().add(menuBox);
        getMenuContentRoot().getChildren().add(new MenuContent());
    }

    private PlatformerMenuBox createMainMenuBody() {

        PlatformerMenuBox platformerMenuBox = new PlatformerMenuBox();

        PlatformerMenuButton newGameButton = new PlatformerMenuButton("New Game");
        newGameButton.setOnAction(actionEvent -> fireNewGame());
        platformerMenuBox.add(newGameButton);

        PlatformerMenuButton optionsButton = new PlatformerMenuButton("Options");
        platformerMenuBox.add(optionsButton);

        PlatformerMenuButton creditsButton = new PlatformerMenuButton("Credits");
        platformerMenuBox.add(creditsButton);

        PlatformerMenuButton exitButton = new PlatformerMenuButton("Exit");
        exitButton.setOnAction(actionEvent -> fireExit());
        platformerMenuBox.add(exitButton);

        return platformerMenuBox;
    }

    @Override
    public void onCreate() {
        FXGL.getAudioPlayer().stopMusic(FXGL.<PlatformerApplication>getAppCast().getGameMusic());
        FXGL.getAudioPlayer().loopMusic(FXGL.<PlatformerApplication>getAppCast().getMainMenuMusic());
        getContentRoot().setCursor(FXGL.<PlatformerApplication>getAppCast().getImageCursor());
    }

    @Override
    public void onExitingTo(Scene nextState) {
        if (nextState instanceof SubScene)
            nextState.getContentRoot().setCursor(getContentRoot().getCursor());
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
    protected Node createBackground(double width, double height) {
        return FXGL.getAssetLoader().loadTexture("menu-background-loop.gif", width, height);
    }

    @Override
    protected Node createTitleView(String tittle) {

        Texture logo = FXGL.texture("logo-texture.png", 500, 340);

        StackPane titleRoot = new StackPane();
        titleRoot.getChildren().addAll(logo);
        titleRoot.setTranslateX(getAppWidth() / 2.0);
        titleRoot.setTranslateY(100);

        return titleRoot;
    }

    @Override
    protected Node createVersionView(String version) {

        Text versionView = new Text(String.format("Woods of Souls (%s)",version));
        versionView.setFont(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(18));
        versionView.setFill(Color.WHITE);
        versionView.setTranslateX(5);
        versionView.setTranslateY(FXGL.getAppHeight() - 5);

        return versionView;
    }

    @Override
    protected Node createProfileView(String profileName) {
        return new Text(profileName);
    }
}
