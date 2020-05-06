package org.monjasa.engine.entities.players.weapons;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.monjasa.engine.entities.players.components.PlayerViewComponent;

public class DaggerStrategy implements WeaponStrategy {

    private StringProperty descriptionProperty;

    public DaggerStrategy() {
        descriptionProperty = new SimpleStringProperty("Dagger");
    }

    @Override
    public void pushAnimation(PlayerViewComponent viewComponent) {
        viewComponent.onMeleeAttack();
    }

    @Override
    public void useWeapon() {
        System.out.println(" - dagger attack - ");
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
