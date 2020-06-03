package org.monjasa.engine.entities;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;

import java.util.List;

public abstract class PlatformerEntityBuilder<T extends PlatformerEntityBuilder, E extends Entity> {

    protected E entity;
    protected PlatformerLevelFactory factory;

    public PlatformerEntityBuilder(PlatformerLevelFactory factory) {
        this.factory = factory;
    }

    protected abstract T getThis();

    public abstract T resetEntity();

    public T loadFromSpawnData(SpawnData spawnData) {

        positionAt(spawnData.getX(), spawnData.getY());

        if (spawnData.hasKey("type")) {
            var typeValue = spawnData.get("type");
            if (typeValue instanceof Enum<?>) addType((Enum<?>) typeValue);
        }

        spawnData.getData().forEach((key, value) -> entity.setProperty(key, value));

        return getThis();
    }

    public T adjustProperty(String propertyKey, Object propertyValue) {
        entity.setProperty(propertyKey, propertyValue);
        return getThis();
    }

    public T addType(Enum<?> type) {
        entity.setType(type);
        return getThis();
    }

    public T positionAt(double x, double y) {
        entity.setPosition(x, y);
        return getThis();
    }

    public T positionAt(Point2D point) {
        entity.setPosition(point);
        return getThis();
    }

    public T centerAt(double x, double y) {
        addScaleOrigin(x, y);
        addRotationOrigin(x, y);
        return getThis();
    }

    public T centerAt(Point2D entityCenter) {
        addScaleOrigin(entityCenter);
        addRotationOrigin(entityCenter);
        return getThis();
    }

    public T layerAt(int zIndex) {
        entity.getTransformComponent().setZ(zIndex);
        return getThis();
    }

    public T addRotationOrigin(double x, double y) {
        entity.getTransformComponent().setRotationOrigin(new Point2D(x, y));
        return getThis();
    }

    public T addRotationOrigin(Point2D point) {
        entity.getTransformComponent().setRotationOrigin(point);
        return getThis();
    }

    public T addScaleOrigin(double x, double y) {
        entity.getTransformComponent().setScaleOrigin(new Point2D(x, y));
        return getThis();
    }

    public T addScaleOrigin(Point2D point) {
        entity.getTransformComponent().setScaleOrigin(point);
        return getThis();
    }

    public T rotationAt(double angle) {
        entity.rotateBy(angle);
        return getThis();
    }

    public T scalingAt(double scaleX, double scaleY) {
        entity.setScaleX(scaleX);
        entity.setScaleY(scaleY);
        return getThis();
    }

    public T scalingAt(Point2D point) {
        scalingAt(point.getX(), point.getY());
        return getThis();
    }

    public T addOpacity(double value) {
        entity.getViewComponent().setOpacity(value);
        return getThis();
    }

    public T addHitBox(HitBox hitBox) {

        entity.getBoundingBoxComponent().addHitBox(hitBox);

        Point2D entityCenter = entity.getBoundingBoxComponent().getCenterLocal();

        entity.getTransformComponent().setScaleOrigin(entityCenter);
        entity.getTransformComponent().setRotationOrigin(entityCenter);

        return getThis();
    }

    public T addView(Node view) {
        entity.getViewComponent().addChild(view);
        return getThis();
    }

    public T addView(String assetName) {
        addView(FXGL.texture(assetName));
        return getThis();
    }

    public T addViewWithHitBox(Node view) {

        entity.getBoundingBoxComponent().clearHitBoxes();

        double viewWidth = view.getLayoutBounds().getWidth();
        double viewHeight = view.getLayoutBounds().getHeight();

        addView(view);
        addHitBox(new HitBox("VIEW", BoundingShape.box(viewWidth, viewHeight)));

        return getThis();
    }

    public T addViewWithHitBox(String assetName) {
        addViewWithHitBox(FXGL.texture(assetName));
        return getThis();
    }

    public T addOnClickAction(Runnable action) {
        entity.getViewComponent().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> action.run());
        return getThis();
    }

    public T attachComponents(Component... components) {
        for (Component component : components)
            entity.addComponent(component);
        return getThis();
    }

    public T attachComponents(List<Component> components) {
        components.forEach(entity::addComponent);
        return getThis();
    }

    public T setCollidable() {
        entity.addComponent(new CollidableComponent(true));
        return getThis();
    }

    public PlatformerLevelFactory getFactory() {
        return factory;
    }

    public void setFactory(PlatformerLevelFactory factory) {
        this.factory = factory;
    }
}