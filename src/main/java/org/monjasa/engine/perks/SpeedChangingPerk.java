package org.monjasa.engine.perks;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import org.monjasa.engine.entities.components.DynamicComponent;

public class SpeedChangingPerk implements Perk {

    private Entity receiver;
    private double valueDifference;
    private int cost;

    public SpeedChangingPerk(Entity receiver, double valueDifference, int cost) {
        this.receiver = receiver;
        this.valueDifference = valueDifference;
        this.cost = cost;
    }

    @Override
    public boolean execute() {

        if (receiver.hasComponent(DynamicComponent.class)) {

            if (FXGL.getWorldProperties().getInt("coins-total-collected") < cost) return false;
            FXGL.inc("coins-total-collected", -cost);

            DynamicComponent dynamicComponent = receiver.getComponent(DynamicComponent.class);
            dynamicComponent.setHorizontalVelocity(dynamicComponent.getHorizontalVelocity() * (1 + valueDifference));

        } else throw new UnsupportedPerkReceiverException(receiver, DynamicComponent.class);

        return true;
    }

    @Override
    public void undo() {

        if (receiver.hasComponent(DynamicComponent.class)) {

            FXGL.inc("coins-total-collected", cost);
            DynamicComponent dynamicComponent = receiver.getComponent(DynamicComponent.class);
            dynamicComponent.setHorizontalVelocity(dynamicComponent.getHorizontalVelocity() / (1 + valueDifference));

        } else throw new UnsupportedPerkReceiverException(receiver, DynamicComponent.class);
    }
}
