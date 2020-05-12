package org.monjasa.engine.entities.players.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import org.monjasa.engine.entities.players.components.PlayerViewComponent;

import static com.almasb.fxgl.dsl.FXGL.*;
import static org.monjasa.engine.entities.PlatformerEntityType.PLAYER;
import static org.monjasa.engine.entities.PlatformerEntityType.PROJECTILE;

public class BowStrategy implements WeaponStrategy {

    private StringProperty descriptionProperty;

    public BowStrategy() {
        descriptionProperty = new SimpleStringProperty("Bow");
    }

    @Override
    public void pushAnimation(PlayerViewComponent viewComponent) {
        viewComponent.onRangedAttack();
    }

    @Override
    public void useWeapon() {

        runOnce(() -> {
            double arrowX = getGameWorld().getSingleton(PLAYER).getX() + 20;
            double arrowY = getGameWorld().getSingleton(PLAYER).getY() + 40;

            double arrowSpeed = 500.0;
            Point2D arrowDirection = new Point2D(getGameWorld().getSingleton(PLAYER).getScaleX(), 0);

            Entity arrow = FXGL.entityBuilder()
                    .type(PROJECTILE)
                    .at(arrowX, arrowY)
                    .viewWithBBox(texture("arrow.png"))
                    .collidable()
                    .with(new ProjectileComponent(arrowDirection, arrowSpeed))
                    .buildAndAttach();

            runOnce(arrow::removeFromWorld, Duration.millis(1000));

        }, Duration.millis(500));
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
