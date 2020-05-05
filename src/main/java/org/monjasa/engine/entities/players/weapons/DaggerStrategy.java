package org.monjasa.engine.entities.players.weapons;

public class DaggerStrategy implements WeaponStrategy {
    @Override
    public void useWeapon() {
        System.out.println("dagger");
    }
}
