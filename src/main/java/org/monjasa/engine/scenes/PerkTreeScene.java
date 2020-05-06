package org.monjasa.engine.scenes;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.scene.Scene;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Pos;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.monjasa.engine.PlatformerApplication;
import org.monjasa.engine.perks.HPChangingPerk;
import org.monjasa.engine.perks.PerkTree;
import org.monjasa.engine.perks.SpeedChangingPerk;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getWorldProperties;

public class PerkTreeScene extends SubScene {

    private PerkTree perkTree;

    public PerkTreeScene() {

        getInput().addAction(new UserAction("Close Perk Tree") {
            @Override
            protected void onActionEnd() {
                FXGL.getSceneService().popSubScene();
            }
        }, KeyCode.F);

        StackPane perkTreePane = new StackPane();

        perkTreePane.getChildren().addAll(
                FXGL.texture("wood-planks.png", 980, 560),
                buildButtonHBox()
        );

        StackPane stackPane = new StackPane(
                new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.rgb(31, 31, 31, 0.50)),
                perkTreePane
        );

        getContentRoot().getChildren().add(stackPane);
    }

    private HBox buildButtonHBox() {

        Texture buttonHPUpgrade = FXGL.texture("potions/health-potion.png", 128, 128);
        buttonHPUpgrade.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            perkTree.executePerk(HPChangingPerk.class);
        });

        Texture buttonSpeedUpgrade = FXGL.texture("potions/speed-potion.png", 128, 128);
        buttonSpeedUpgrade.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            perkTree.executePerk(SpeedChangingPerk.class);
        });

        Texture buttonUndo = FXGL.texture("potions/revert-potion.png", 128, 128);
        buttonUndo.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            perkTree.undoLastPerk();
        });

        HBox hBox = new HBox(buttonHPUpgrade, buttonSpeedUpgrade, new Rectangle(128, 128, Color.TRANSPARENT), buttonUndo);
        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }

    @Override
    public void onCreate() {
        perkTree = getWorldProperties().getObject("perkTree");
    }

    @Override
    public void onEnteredFrom(Scene prevState) {
        if (prevState instanceof GameScene) {
            getGameScene().getContentRoot().getChildren().forEach(node -> node.setEffect(new GaussianBlur(5)));
        }
    }

    @Override
    public void onExitingTo(Scene nextState) {
        if (nextState instanceof GameScene) {
            perkTree.closePerkTree();
            getGameScene().getContentRoot().getChildren().forEach(node -> node.setEffect(null));
        }
    }
}
