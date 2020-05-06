package org.monjasa.engine.entities.players.weapons;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.monjasa.engine.entities.players.components.PlayerViewComponent;

public class BowStrategy implements WeaponStrategy {

    private StringProperty descriptionProperty;

    public BowStrategy() {
        descriptionProperty = new SimpleStringProperty("Bow");
    }

    @Override
    public void pushAnimation(PlayerViewComponent viewComponent) {
        viewComponent.onRangedAttack();
    }

    @Override
    public void useWeapon() {
        System.out.println(" - bow attack - ");
    }

    @Override
    public StringProperty descriptionProperty() {
        return descriptionProperty;
    }

    @Override
    public String getDescription() {
        return descriptionProperty.getValue();
    }
}
