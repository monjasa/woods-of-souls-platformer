package org.monjasa.engine.perks;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import org.monjasa.engine.entities.components.EntityHPComponent;

public class HPChangingPerk implements Perk {

    private transient Entity receiver;
    private int valueDifference;
    private int cost;

    public HPChangingPerk(Entity receiver, int valueDifference, int cost) {
        this.receiver = receiver;
        this.valueDifference = valueDifference;
        this.cost = cost;
    }

    @Override
    public boolean execute() {

        if (receiver.hasComponent(EntityHPComponent.class)) {

            if (FXGL.getWorldProperties().getInt("coins-total-collected") < cost) return false;
            FXGL.inc("coins-total-collected", -cost);

            EntityHPComponent hpComponent = receiver.getComponent(EntityHPComponent.class);
            hpComponent.expandValue(valueDifference);

        } else throw new UnsupportedPerkReceiverException(receiver, EntityHPComponent.class);

        return true;
    }

    @Override
    public void undo() {

        if (receiver.hasComponent(EntityHPComponent.class)) {

            FXGL.inc("coins-total-collected", cost);
            EntityHPComponent hpComponent = receiver.getComponent(EntityHPComponent.class);
            hpComponent.expandValue(-valueDifference);

        } else throw new UnsupportedPerkReceiverException(receiver, EntityHPComponent.class);
    }

    public Entity getReceiver() {
        return receiver;
    }

    public void setReceiver(Entity receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "HPChangingPerk{" +
                "receiver=" + receiver +
                ", valueDifference=" + valueDifference +
                ", cost=" + cost +
                '}';
    }
}
