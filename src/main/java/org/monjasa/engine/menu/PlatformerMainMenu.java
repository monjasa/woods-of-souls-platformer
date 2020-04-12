package org.monjasa.engine.menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

public class PlatformerMainMenu extends FXGLMenu {

    public PlatformerMainMenu(@NotNull MenuType type) {
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
//        optionsButton.setOnAction(actionEvent -> fireNewGame());
        platformerMenuBox.add(optionsButton);

        PlatformerMenuButton creditsButton = new PlatformerMenuButton("Credits");
//        creditsButton.setOnAction(actionEvent -> fireNewGame());
        platformerMenuBox.add(creditsButton);

        PlatformerMenuButton exitButton = new PlatformerMenuButton("Exit");
        exitButton.setOnAction(actionEvent -> fireExit());
        platformerMenuBox.add(exitButton);

        return platformerMenuBox;
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
    protected Node createBackground(double width, double height) {
        return FXGL.getAssetLoader().loadTexture("dark-forest-main-menu-background.jpg", width, height);
    }

    @NotNull
    @Override
    protected Node createTitleView(@NotNull String tittle) {
        Texture logo = FXGL.texture("logo-texture.png", 500, 340);

        StackPane titleRoot = new StackPane();
        titleRoot.getChildren().addAll(logo);
        titleRoot.setTranslateX(getAppWidth() / 2.0);
        titleRoot.setTranslateY(100);

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
    protected Node createProfileView(@NotNull String profileName) {
        return new Text(profileName);
    }
}
