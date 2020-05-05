package org.monjasa.engine.perks;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import org.monjasa.engine.PlatformerApplication;
import org.monjasa.engine.entities.components.DynamicComponent;

public class SpeedChangingPerk implements Perk {

    private double valueDifference;
    private int cost;

    public SpeedChangingPerk(double valueDifference, int cost) {
        this.valueDifference = valueDifference;
        this.cost = cost;
    }

    @Override
    public boolean execute(Entity receiver) {

        if (receiver.hasComponent(DynamicComponent.class)) {

            if (FXGL.getWorldProperties().getInt("coinsAvailable") < cost) return false;
            FXGL.<PlatformerApplication>getAppCast().changeCoinsAvailableValue(-cost);

            DynamicComponent dynamicComponent = receiver.getComponent(DynamicComponent.class);
            dynamicComponent.setHorizontalVelocity(dynamicComponent.getHorizontalVelocity() + valueDifference);

        } else throw new UnsupportedPerkReceiverException(receiver, DynamicComponent.class);

        return true;
    }

    @Override
    public void undo(Entity receiver) {

        if (receiver.hasComponent(DynamicComponent.class)) {

            FXGL.<PlatformerApplication>getAppCast().changeCoinsAvailableValue(cost);
            DynamicComponent dynamicComponent = receiver.getComponent(DynamicComponent.class);
            dynamicComponent.setHorizontalVelocity(dynamicComponent.getHorizontalVelocity() - valueDifference);

        } else throw new UnsupportedPerkReceiverException(receiver, DynamicComponent.class);
    }

    @Override
    public String toString() {
        return "SpeedChangingPerk{" +
                "valueDifference=" + valueDifference +
                ", cost=" + cost +
                '}';
    }
}
