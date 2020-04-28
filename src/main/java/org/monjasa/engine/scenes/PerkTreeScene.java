package org.monjasa.engine.scenes;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.scene.Scene;
import com.almasb.fxgl.scene.SubScene;
import javafx.geometry.Pos;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.monjasa.engine.PlatformerApplication;
import org.monjasa.engine.perks.PerkTree;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;

public class PerkTreeScene extends SubScene {

    private PerkTree perkTree;

    public PerkTreeScene() {

        getInput().addAction(new UserAction("Close Perk Tree") {
            @Override
            protected void onActionEnd() {
                requestHide();
            }
        }, KeyCode.F);

        perkTree = FXGL.<PlatformerApplication>getAppCast().getPerkTree();

        StackPane perkTreePane = new StackPane();

        perkTreePane.getChildren().addAll(
                new Rectangle(900, 500, Color.BLUE),
                buildButtonHBox()
        );

        StackPane stackPane = new StackPane(
                new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.rgb(31, 31, 31, 0.50)),
                perkTreePane
        );

        getContentRoot().getChildren().add(stackPane);
    }

    private HBox buildButtonHBox() {

        Rectangle buttonHPUpgrade = new Rectangle(80, 80, Color.GREEN);
        buttonHPUpgrade.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            perkTree.executePerk(0);
        });

        Rectangle buttonSpeedUpgrade = new Rectangle(80, 80, Color.PURPLE);
        buttonSpeedUpgrade.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            perkTree.executePerk(1);
        });

        Rectangle buttonUndo = new Rectangle(80, 80, Color.RED);
        buttonUndo.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            perkTree.undoLastPerk();
        });

        HBox hBox = new HBox(buttonHPUpgrade, buttonSpeedUpgrade, buttonUndo);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
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
            getGameScene().getContentRoot().getChildren().forEach(node -> node.setEffect(null));
            perkTree.clearPerkDeque();
        }
    }

    public void requestHide() {
        FXGL.getSceneService().popSubScene();
    }
}
