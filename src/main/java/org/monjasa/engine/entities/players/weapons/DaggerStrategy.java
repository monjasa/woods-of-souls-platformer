package org.monjasa.engine.entities.players.weapons;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.monjasa.engine.entities.players.components.PlayerViewComponent;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static org.monjasa.engine.entities.PlatformerEntityType.PLAYER;
import static org.monjasa.engine.entities.PlatformerEntityType.PROJECTILE;

public class DaggerStrategy implements WeaponStrategy {

    private StringProperty descriptionProperty;

    public DaggerStrategy() {
        descriptionProperty = new SimpleStringProperty("Dagger");
    }

    @Override
    public void pushAnimation(PlayerViewComponent viewComponent) {
        viewComponent.onMeleeAttack();
    }

    @Override
    public void useWeapon() {

        double attackAreaX = getGameWorld().getSingleton(PLAYER).getRightX();
        double attackAreaY = getGameWorld().getSingleton(PLAYER).getY();

        Entity attackArea = FXGL.entityBuilder()
                .type(PROJECTILE)
                .at(attackAreaX, attackAreaY)
                .viewWithBBox(new Rectangle(20, getGameWorld().getSingleton(PLAYER).getHeight(), Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();

        runOnce(attackArea::removeFromWorld, Duration.millis(100));
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
