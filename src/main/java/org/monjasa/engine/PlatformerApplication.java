package org.monjasa.engine;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlatformerApplication extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.0.2");
    }

    private Entity player;

    @Override
    protected void initGame() {

        player = createPlayer(new Point2D(100, 100));

        getGameWorld().addEntity(player);
        getGameWorld().addEntity(createPlatform(new Point2D(80, 400), 200, 40));
        getGameWorld().addEntity(createPlatform(new Point2D(360, 280), 280, 40));
    }

    private Entity createPlayer(Point2D position) {

        Entity player = new Entity();
        player.setPosition(position);

        Node playerView = new Rectangle(50, 50, Color.DARKBLUE);
        player.getViewComponent().addChild(playerView);

        double viewWidth = playerView.getLayoutBounds().getWidth();
        double viewHeight = playerView.getLayoutBounds().getHeight();

        HitBox playerHitBox = new HitBox("VIEW", BoundingShape.box(viewWidth, viewHeight));
        player.getBoundingBoxComponent().clearHitBoxes();
        player.getBoundingBoxComponent().addHitBox(playerHitBox);

        Point2D entityCenter = player.getBoundingBoxComponent().getCenterLocal();
        player.getTransformComponent().setScaleOrigin(entityCenter);
        player.getTransformComponent().setRotationOrigin(entityCenter);

        PhysicsComponent playerPhysicsComponent = new PhysicsComponent();
        playerPhysicsComponent.setBodyType(BodyType.DYNAMIC);
        player.addComponent(playerPhysicsComponent);

        return player;
    }

    private Entity createPlatform(Point2D position, double width, double height) {

        Entity platform = new Entity();

        platform.setPosition(position);

        Node platformView = new Rectangle(width, height, Color.BLACK);
        platform.getViewComponent().addChild(platformView);

        HitBox platformHitBox = new HitBox("VIEW", BoundingShape.box(width, height));
        platform.getBoundingBoxComponent().clearHitBoxes();
        platform.getBoundingBoxComponent().addHitBox(platformHitBox);

        Point2D entityCenter = platform.getBoundingBoxComponent().getCenterLocal();
        platform.getTransformComponent().setScaleOrigin(entityCenter);
        platform.getTransformComponent().setRotationOrigin(entityCenter);

        platform.addComponent(new PhysicsComponent());

        return platform;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
