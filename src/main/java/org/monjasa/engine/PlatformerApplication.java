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
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.monjasa.engine.components.PlayerControlComponent;
import org.monjasa.engine.components.PlayerViewComponent;
import org.monjasa.engine.entities.EntityBuilder;
import org.monjasa.engine.entities.PlatformerEntityBuilder;
import org.monjasa.engine.entities.PlatformerEntityType;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;

public class PlatformerApplication extends GameApplication {

    private Entity player;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Woods of Souls");
        settings.setVersion("0.1.1");
    }

    @Override
    protected void initGame() {

        PhysicsComponent playerPhysicsComponent = new PhysicsComponent();
        playerPhysicsComponent.setBodyType(BodyType.DYNAMIC);
        playerPhysicsComponent.setFixtureDef(new FixtureDef().friction(0.0f));

        player = getEntityBuilder()
                .addType(PlatformerEntityType.PLAYER)
                .positionAt(120, 120)
                .addHitBox(new HitBox(BoundingShape.box(32, 42)))
                .attachComponents(playerPhysicsComponent, new PlayerViewComponent(), new PlayerControlComponent())
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
                player.getComponent(PlayerControlComponent.class).moveRight();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControlComponent.class).stopHorizontalMoving();
            }
        }, KeyCode.RIGHT);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControlComponent.class).moveLeft();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerControlComponent.class).stopHorizontalMoving();
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerControlComponent.class).jump();
            }
        }, KeyCode.UP);
    }

    public static EntityBuilder getEntityBuilder() {
        return new PlatformerEntityBuilder();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
