package org.monjasa.engine.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.monjasa.engine.observer.Observer;

import static com.almasb.fxgl.dsl.FXGL.texture;

public class CoinsUIElement extends StackPane implements Observer {

    private IntegerProperty availableCoinsProperty;

    public CoinsUIElement() {

        availableCoinsProperty = new SimpleIntegerProperty();

        Text coinsCollectedText = new Text();
        coinsCollectedText.fontProperty().setValue(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(36));

        coinsCollectedText.textProperty().bind(availableCoinsProperty.asString());

        BorderPane textPane = new BorderPane(coinsCollectedText);
        textPane.setPadding(new Insets(0, 0, 0, 20));

        Texture border = texture("ui-border.png");

        getChildren().addAll(
                border,
                textPane
        );
    }

    @Override
    public void update(int availableCoins) {
        this.availableCoinsProperty.setValue(availableCoins);
    }
}
