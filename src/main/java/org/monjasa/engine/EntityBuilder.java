package org.monjasa.engine;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public interface EntityBuilder {

    EntityBuilder resetEntity();

    EntityBuilder loadFromSpawnData(SpawnData spawnData);

    EntityBuilder adjustProperty(String propertyKey, Object propertyValue);

    EntityBuilder addType(Enum<?> type);

    EntityBuilder positionAt(double x, double y);

    EntityBuilder positionAt(Point2D point);

    EntityBuilder layerAt(int zIndex);

    EntityBuilder addRotationOrigin(double x, double y);

    EntityBuilder addRotationOrigin(Point2D point);

    EntityBuilder addScaleOrigin(double x, double y);

    EntityBuilder addScaleOrigin(Point2D point);

    EntityBuilder rotationAt(double angle);

    EntityBuilder scalingAt(double scaleX, double scaleY);

    EntityBuilder scalingAt(Point2D point);

    EntityBuilder addOpacity(double value);

    EntityBuilder addHitBox(HitBox hitBox);

    EntityBuilder addView(Node view);

    EntityBuilder addView(String assetName);

    EntityBuilder addViewWithHitBox(Node view);

    EntityBuilder addViewWithHitBox(String assetName);

    EntityBuilder addOnClickAction(Runnable action);

    EntityBuilder attachComponents(Component... components);

    EntityBuilder setCollidable();

    Entity buildEntity();
}
