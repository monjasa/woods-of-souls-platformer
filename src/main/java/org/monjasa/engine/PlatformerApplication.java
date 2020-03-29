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

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlatformerApplication extends GameApplication {

    private static final int PLAYER_MOVE_SPEED = 200;
    private static final int PLAYER_JUMP_SPEED = 400;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.0.4");
    }

    private Entity player;

    @Override
    protected void initGame() {

        PhysicsComponent playerPhysicsComponent = new PhysicsComponent();
        playerPhysicsComponent.setBodyType(BodyType.DYNAMIC);

        player = getEntityBuilder()
                .addType(PlatformerEntityType.PLAYER)
                .positionAt(120, 120)
                .addViewWithHitBox("player.png")
                .attachComponents(playerPhysicsComponent)
                .buildEntity();

        List<Entity> platforms = new ArrayList<>();

        platforms.add(getEntityBuilder()
                .addType(PlatformerEntityType.PLATFORM)
                .positionAt(80, 400)
                .addViewWithHitBox(new Rectangle(200, 40, Color.BLACK))
                .attachComponents(new PhysicsComponent())
                .buildEntity());

        platforms.add(getEntityBuilder()
                .addType(PlatformerEntityType.PLATFORM)
                .positionAt(360, 280)
                .addViewWithHitBox(new Rectangle(280, 40, Color.BLACK))
                .attachComponents(new PhysicsComponent())
                .buildEntity());

        getGameWorld().addEntity(player);
        platforms.forEach(getGameWorld()::addEntity);
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

    public static EntityBuilder getEntityBuilder() {
        return new PlatformerEntityBuilder();
    }
}
