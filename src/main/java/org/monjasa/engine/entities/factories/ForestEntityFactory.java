package org.monjasa.engine.entities.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.monjasa.engine.PlatformerApplication;
import org.monjasa.engine.entities.PlatformerEntityType;
import org.monjasa.engine.entities.exits.Exit;
import org.monjasa.engine.entities.exits.ExitBuilder;
import org.monjasa.engine.entities.exits.ForestExit;
import org.monjasa.engine.entities.platforms.ForestPlatform;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.platforms.PlatformBuilder;
import org.monjasa.engine.entities.players.ForestPlayer;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.entities.players.PlayerBuilder;

import java.net.URL;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ForestEntityFactory implements PlatformerEntityFactory {

    private static final int MAX_LEVEL = 1;
    private static final String DEVELOPING_LEVEL_PATH = "assets/levels/tmx/forest_dev.tmx";

    @Override
    public Platform getPlatformInstance() {
        return new ForestPlatform();
    }

    @Override
    @Spawns("forest-platform")
    public Platform createPlatform(SpawnData data) {
        return new PlatformBuilder(this)
                .loadFromSpawnData(data)
                .addType(PlatformerEntityType.PLATFORM)
                .addHitBox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .attachComponents(new PhysicsComponent())
                .buildPlatform();
    }

    @Override
    public Player getPlayerInstance() {
        return new ForestPlayer();
    }

    @Override
    @Spawns("forest-player")
    public Player createPlayer(SpawnData data) {

        PhysicsComponent playerPhysicsComponent = new PhysicsComponent();
        playerPhysicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(35, 209),
                BoundingShape.box(50, 2)));
        playerPhysicsComponent.setBodyType(BodyType.DYNAMIC);
        playerPhysicsComponent.setFixtureDef(new FixtureDef().friction(0.0f));

        return new PlayerBuilder(this)
                .loadFromSpawnData(data)
                .addType(PlatformerEntityType.PLAYER)
                .centerAt(data.<Integer>get("width") / 2.0, data.<Integer>get("height") / 2.0)
                .addHitBox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .attachComponents(playerPhysicsComponent, new ForestPlayer.ForestPlayerViewComponent(),
                        new ForestPlayer.ForestPlayerControlComponent())
                .setCollidable()
                .buildPlayer();
    }

    @Override
    public Exit getExitInstance() {
        return new ForestExit();
    }

    @Override
    @Spawns("forest-exit")
    public Exit createExit(SpawnData data) {
        return new ExitBuilder(this)
                .loadFromSpawnData(data)
                .addType(PlatformerEntityType.EXIT)
                .addHitBox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .setCollidable()
                .buildExit();
    }

    @Override
    public Level createLevel(int levelNum, boolean isDevelopingNewLevel) {

        Level level;
        URL path = getClass().getClassLoader().getResource(DEVELOPING_LEVEL_PATH);

        if (isDevelopingNewLevel && path != null) {
            level = new TMXLevelLoader().load(path, getGameWorld());
        } else {
            level = getAssetLoader().loadLevel(String.format("tmx/forest_%02d.tmx", levelNum), new TMXLevelLoader());
        }

        return level;
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }
}