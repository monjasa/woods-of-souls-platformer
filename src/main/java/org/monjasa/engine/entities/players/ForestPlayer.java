package org.monjasa.engine.entities.players;

import org.monjasa.engine.entities.players.components.ForestPlayerControlComponent;
import org.monjasa.engine.entities.players.components.ForestPlayerViewComponent;
import org.monjasa.engine.entities.components.EntityHPComponent;
import org.monjasa.engine.entities.players.components.PlayerViewComponent;
import org.monjasa.engine.entities.players.weapons.BowStrategy;
import org.monjasa.engine.entities.players.weapons.DaggerStrategy;
import org.monjasa.engine.entities.players.weapons.WeaponStrategy;
import org.monjasa.engine.util.CircularQueue;

public class ForestPlayer extends Player {

    public ForestPlayer() {
        weaponStrategies = new CircularQueue<>(WeaponStrategy.class, 2);
        weaponStrategies.addElement(new DaggerStrategy());
        weaponStrategies.addElement(new BowStrategy());
        currentWeaponStrategyProperty.setValue(weaponStrategies.peekNextElement());
    }

    @Override
    public Player attachPlayerComponents() {
        playerViewComponent = getComponent(ForestPlayerViewComponent.class);
        playerControlComponent = getComponent(ForestPlayerControlComponent.class);
        playerHPComponent = getComponent(EntityHPComponent.class);
        return this;
    }

    @Override
    public void attack() {
        currentWeaponStrategyProperty.getValue().useWeapon();
        currentWeaponStrategyProperty.getValue().pushAnimation(getComponent(ForestPlayerViewComponent.class));
    }
}
