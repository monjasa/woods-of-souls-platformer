package org.monjasa.engine.entities.players.weapons;

import javafx.beans.property.StringProperty;
import org.monjasa.engine.entities.players.components.PlayerViewComponent;

public interface WeaponStrategy {

    void useWeapon();

    void pushAnimation(PlayerViewComponent viewComponent);

    StringProperty descriptionProperty();

    String getDescription();
}
