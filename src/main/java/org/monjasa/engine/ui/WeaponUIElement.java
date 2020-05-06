package org.monjasa.engine.ui;

import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.monjasa.engine.entities.components.EntityHPComponent;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.entities.players.weapons.WeaponStrategy;

public class WeaponUIElement extends StackPane implements UpdatableUIElement {

    private Property<WeaponStrategy> currentWeaponStrategyProperty;

    private Text weaponDescription;

    public WeaponUIElement(Player player) {

        weaponDescription = new Text();
        weaponDescription.fontProperty().setValue(FXGL.getAssetLoader().loadFont("gnomoria.ttf").newFont(48));
        weaponDescription.fillProperty().setValue(Color.WHITE);

        currentWeaponStrategyProperty = player.currentWeaponStrategyProperty();
        weaponDescription.textProperty().bind(currentWeaponStrategyProperty.getValue().descriptionProperty());
        currentWeaponStrategyProperty.addListener((observable, oldValue, newValue) -> {
            weaponDescription.textProperty().bind(newValue.descriptionProperty());
        });

        getChildren().addAll(weaponDescription);
    }

    public void updatePlayer(Player player) {
        currentWeaponStrategyProperty = player.currentWeaponStrategyProperty();
        weaponDescription.textProperty().bind(currentWeaponStrategyProperty.getValue().descriptionProperty());
        currentWeaponStrategyProperty.addListener((observable, oldValue, newValue) -> {
            weaponDescription.textProperty().bind(newValue.descriptionProperty());
        });
    }
}
