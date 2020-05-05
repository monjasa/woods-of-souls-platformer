package org.monjasa.engine.entities.players.weapons;

public class BowStrategy implements WeaponStrategy {
    @Override
    public void useWeapon() {
        System.out.println("bow");
    }
}
