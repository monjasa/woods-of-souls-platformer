package org.monjasa.engine.perks;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import org.monjasa.engine.entities.components.EntityHPComponent;

public class HPChangingPerk implements Perk {

    private int valueDifference;
    private int cost;

    public HPChangingPerk(int valueDifference, int cost) {
        this.valueDifference = valueDifference;
        this.cost = cost;
    }

    @Override
    public boolean execute(Entity receiver) {

        if (receiver.hasComponent(EntityHPComponent.class)) {

            if (FXGL.getWorldProperties().getInt("coinsAvailable") < cost) return false;
            FXGL.inc("coinsAvailable", -cost);

            EntityHPComponent hpComponent = receiver.getComponent(EntityHPComponent.class);
            hpComponent.expandValue(valueDifference);

        } else throw new UnsupportedPerkReceiverException(receiver, EntityHPComponent.class);

        return true;
    }

    @Override
    public void undo(Entity receiver) {

        if (receiver.hasComponent(EntityHPComponent.class)) {

            FXGL.inc("coinsAvailable", cost);
            EntityHPComponent hpComponent = receiver.getComponent(EntityHPComponent.class);
            hpComponent.expandValue(-valueDifference);

        } else throw new UnsupportedPerkReceiverException(receiver, EntityHPComponent.class);
    }

    @Override
    public String toString() {
        return "HPChangingPerk{" +
                "valueDifference=" + valueDifference +
                ", cost=" + cost +
                '}';
    }
}
