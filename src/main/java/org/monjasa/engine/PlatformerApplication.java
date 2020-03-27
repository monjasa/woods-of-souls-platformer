package org.monjasa.engine;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlatformerApplication extends GameApplication {

    private static final int PLAYER_MOVE_SPEED = 200;
    private static final int PLAYER_JUMP_SPEED = 400;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.0.3");
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

//        Node playerView = new Rectangle(50, 50, Color.DARKBLUE);
        Node playerView = new ImageView(image("player.png"));
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

    @Override
    protected void initInput() {

        Input input = getInput();

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                player.getComponent(PhysicsComponent.class).setVelocityX(PLAYER_MOVE_SPEED);
                player.setScaleX(1);
            }

            @Override
            protected void onActionEnd() {
                stopMoving(player);
            }
        }, KeyCode.RIGHT);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.getComponent(PhysicsComponent.class).setVelocityX(-PLAYER_MOVE_SPEED);
                player.setScaleX(-1);
            }

            @Override
            protected void onActionEnd() {
                stopMoving(player);
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                player.getComponent(PhysicsComponent.class).setVelocityY(-PLAYER_JUMP_SPEED);
            }
        }, KeyCode.UP);
    }

    private void stopMoving(Entity entity) {
        entity.getComponent(PhysicsComponent.class).setVelocityX(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
