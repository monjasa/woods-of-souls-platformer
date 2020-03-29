package org.monjasa.engine;

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

public class PlatformerEntityBuilder implements EntityBuilder {

    private Entity entity;

    public PlatformerEntityBuilder() {
        entity = new Entity();
    }

    @Override
    public EntityBuilder resetEntity() {
        entity = new Entity();
        return this;
    }

    @Override
    public EntityBuilder loadFromSpawnData(SpawnData spawnData) {

        positionAt(spawnData.getX(), spawnData.getY());

        if (spawnData.hasKey("type")) {
            var typeValue = spawnData.get("type");
            if (typeValue instanceof Enum<?>) addType((Enum<?>) typeValue);
        }

        spawnData.getData().forEach((key, value) -> entity.setProperty(key, value));

        return this;
    }

    @Override
    public EntityBuilder adjustProperty(String propertyKey, Object propertyValue) {
        entity.setProperty(propertyKey, propertyValue);
        return this;
    }

    @Override
    public EntityBuilder addType(Enum<?> type) {
        entity.setType(type);
        return this;
    }

    @Override
    public EntityBuilder positionAt(double x, double y) {
        entity.setPosition(x, y);
        return this;
    }

    @Override
    public EntityBuilder positionAt(Point2D point) {
        entity.setPosition(point);
        return this;
    }

    @Override
    public EntityBuilder layerAt(int zIndex) {
        entity.getTransformComponent().setZ(zIndex);
        return this;
    }

    @Override
    public EntityBuilder addRotationOrigin(double x, double y) {
        entity.getTransformComponent().setRotationOrigin(new Point2D(x, y));
        return this;
    }

    @Override
    public EntityBuilder addRotationOrigin(Point2D point) {
        entity.getTransformComponent().setRotationOrigin(point);
        return this;
    }

    @Override
    public EntityBuilder addScaleOrigin(double x, double y) {
        entity.getTransformComponent().setScaleOrigin(new Point2D(x, y));
        return this;
    }

    @Override
    public EntityBuilder addScaleOrigin(Point2D point) {
        entity.getTransformComponent().setScaleOrigin(point);
        return this;
    }

    @Override
    public EntityBuilder rotationAt(double angle) {
        entity.rotateBy(angle);
        return this;
    }

    @Override
    public EntityBuilder scalingAt(double scaleX, double scaleY) {
        entity.setScaleX(scaleX);
        entity.setScaleY(scaleY);
        return this;
    }

    @Override
    public EntityBuilder scalingAt(Point2D point) {
        scalingAt(point.getX(), point.getY());
        return this;
    }

    @Override
    public EntityBuilder addOpacity(double value) {
        entity.getViewComponent().setOpacity(value);
        return this;
    }

    @Override
    public EntityBuilder addHitBox(HitBox hitBox) {

        entity.getBoundingBoxComponent().addHitBox(hitBox);

        Point2D entityCenter = entity.getBoundingBoxComponent().getCenterLocal();

        entity.getTransformComponent().setScaleOrigin(entityCenter);
        entity.getTransformComponent().setRotationOrigin(entityCenter);

        return this;
    }

    @Override
    public EntityBuilder addView(Node view) {
        entity.getViewComponent().addChild(view);
        return this;
    }

    @Override
    public EntityBuilder addView(String assetName) {
        addView(FXGL.texture(assetName));
        return this;
    }

    @Override
    public EntityBuilder addViewWithHitBox(Node view) {

        entity.getBoundingBoxComponent().clearHitBoxes();

        double viewWidth = view.getLayoutBounds().getWidth();
        double viewHeight = view.getLayoutBounds().getHeight();

        addView(view);
        addHitBox(new HitBox("VIEW", BoundingShape.box(viewWidth, viewHeight)));

        return this;
    }

    @Override
    public EntityBuilder addViewWithHitBox(String assetName) {
        addViewWithHitBox(FXGL.texture(assetName));
        return this;
    }

    @Override
    public EntityBuilder addOnClickAction(Runnable action) {
        entity.getViewComponent().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> action.run());
        return this;
    }

    @Override
    public EntityBuilder attachComponents(Component... components) {
        for (Component component : components)
            entity.addComponent(component);
        return this;
    }

    @Override
    public EntityBuilder setCollidable() {
        entity.addComponent(new CollidableComponent(true));
        return this;
    }

    @Override
    public Entity buildEntity() {
        return entity;
    }
}
