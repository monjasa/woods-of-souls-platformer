package org.monjasa.engine.entities.players;

import com.almasb.fxgl.core.Updatable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import org.monjasa.engine.PlatformerApplication;
import org.monjasa.engine.entities.components.EntityHPComponent;
import org.monjasa.engine.entities.enemies.Enemy;
import org.monjasa.engine.entities.players.components.PlayerControlComponent;
import org.monjasa.engine.entities.players.components.PlayerViewComponent;
import org.monjasa.engine.entities.players.weapons.WeaponStrategy;
import org.monjasa.engine.util.CircularQueue;

import static com.almasb.fxgl.dsl.FXGL.runOnce;

public abstract class Player extends Entity implements Updatable {

    PlayerViewComponent playerViewComponent;
    PlayerControlComponent playerControlComponent;
    EntityHPComponent playerHPComponent;

    CircularQueue<WeaponStrategy> weaponStrategies;
    Property<WeaponStrategy> currentWeaponStrategyProperty = new SimpleObjectProperty<>();

    public abstract Player attachPlayerComponents();

    public abstract void attack();

    @Override
    public void onUpdate(double tpf) {
        if (playerControlComponent.isMovingVertically()) playerViewComponent.onMovingVertically();
    }

    public void onEnemyHit(Enemy enemy) {

        enemy.hitPlayer(this);

        if (playerHPComponent.getValue() <= 0) {
            FXGL.<PlatformerApplication>getAppCast().onPlayerDied();
            return;
        }

        Point2D dmgVector = getPosition().subtract(enemy.getPosition());

        getComponent(PhysicsComponent.class).setLinearVelocity(new Point2D(Math.signum(dmgVector.getX()) * 290, -300));

        runOnce(() -> {
            try {
                getComponent(PhysicsComponent.class).setVelocityX(0);
            } catch (IllegalArgumentException ignored) {
            }
        }, Duration.millis(1000));
    }

    public void goLeft() {
        playerControlComponent.moveLeft();
        playerViewComponent.onMovingHorizontally();
    }

    public void goRight() {
        playerControlComponent.moveRight();
        playerViewComponent.onMovingHorizontally();
    }

    public void horizontalStop() {
        playerControlComponent.stopHorizontalMoving();
        playerViewComponent.onHorizontalStop();
    }

    public void goUp() {
        playerControlComponent.jump();
    }

    public void switchWeapon() {
        currentWeaponStrategyProperty.setValue(weaponStrategies.peekNextElement());
    }

    public PlayerViewComponent getPlayerViewComponent() {
        return playerViewComponent;
    }

    public PlayerControlComponent getPlayerControlComponent() {
        return playerControlComponent;
    }

    public Property<WeaponStrategy> currentWeaponStrategyProperty() {
        return currentWeaponStrategyProperty;
    }

    public WeaponStrategy getCurrentWeaponStrategy() {
        return currentWeaponStrategyProperty.getValue();
    }
}
