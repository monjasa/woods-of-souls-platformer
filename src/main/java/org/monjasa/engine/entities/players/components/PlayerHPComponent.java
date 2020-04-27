package org.monjasa.engine.entities.players.components;

import com.almasb.fxgl.entity.components.IntegerComponent;

public class PlayerHPComponent extends IntegerComponent {

    private int maxHP;

    public PlayerHPComponent(int initialHP) {
        super(initialHP);
        maxHP = initialHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
