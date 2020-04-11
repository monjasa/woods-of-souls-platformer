package org.monjasa.engine.entities.factories;

import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.monjasa.engine.entities.PlatformerEntityType;
import org.monjasa.engine.entities.platforms.ForestPlatform;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.platforms.PlatformBuilder;
import org.monjasa.engine.entities.players.ForestPlayer;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.entities.players.PlayerBuilder;

public class ForestEntityFactory implements PlatformerEntityFactory {

    @Override
    public Platform getPlatformInstance() {
        return new ForestPlatform();
    }

    @Override
    @Spawns("forest-platform")
    public Platform createPlatform(SpawnData data) {
        return (Platform) new PlatformBuilder(this).loadFromSpawnData(data)
                .addType(PlatformerEntityType.PLATFORM)
                .addHitBox(new HitBox(BoundingShape.box(300, 50)))
//                .addHitBox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .addView(new Rectangle(300, 50, Color.BLACK))
                .attachComponents(new PhysicsComponent())
                .buildEntity();
    }

    @Override
    public Player getPlayerInstance() {
        return new ForestPlayer();
    }

    @Override
    @Spawns("forest-player")
    public Player createPlayer(SpawnData data) {

        PhysicsComponent playerPhysicsComponent = new PhysicsComponent();
        playerPhysicsComponent.setBodyType(BodyType.DYNAMIC);
        playerPhysicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(15, 45), BoundingShape.box(20, 10)));
        playerPhysicsComponent.setFixtureDef(new FixtureDef().friction(0.0f));

        return (Player) new PlayerBuilder(this).loadFromSpawnData(data)
                .addType(PlatformerEntityType.PLAYER)
                .addHitBox(new HitBox(BoundingShape.box(50, 50)))
//                .addHitBox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .addView(new Rectangle(50, 50, Color.BLUE))
                .attachComponents(playerPhysicsComponent)
                .buildEntity();
    }
}