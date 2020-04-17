package org.monjasa.engine.entities.factories;

import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import org.monjasa.engine.entities.PlatformerEntityType;
import org.monjasa.engine.entities.SimpleEntityBuilder;
import org.monjasa.engine.entities.exits.Exit;
import org.monjasa.engine.entities.exits.ExitBuilder;
import org.monjasa.engine.entities.exits.ForestExit;
import org.monjasa.engine.entities.platforms.ForestPlatform;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.platforms.PlatformBuilder;
import org.monjasa.engine.entities.players.ForestPlayer;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.entities.players.PlayerBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ForestLevelFactory extends PlatformerLevelFactory {

    private static final String FOREST_LEVEL_PREFIX = "forest";
    private static final String FOREST_DEVELOPING_LEVEL_NAME = "level_dev";

    public ForestLevelFactory(int maxLevel) {
        super(maxLevel, FOREST_LEVEL_PREFIX, FOREST_DEVELOPING_LEVEL_NAME);
    }

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
        playerPhysicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(25, 164),
                BoundingShape.box(40, 2)));
        playerPhysicsComponent.setBodyType(BodyType.DYNAMIC);
        playerPhysicsComponent.setFixtureDef(new FixtureDef().friction(0.0f));

        HitBox playerMainHitBox = new HitBox("PLAYER_HITBOX",
                new Point2D(20, 0),
                BoundingShape.box(50, 165));

        return new PlayerBuilder(this)
                .loadFromSpawnData(data)
                .addType(PlatformerEntityType.PLAYER)
                .centerAt(data.<Integer>get("width") / 2.0, data.<Integer>get("height") / 2.0)
                .addHitBox(playerMainHitBox)
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

        Level level = super.createLevel(levelNum, isDevelopingNewLevel);

        int backgroundParallaxIndex = 0;
        int foregroundParallaxIndex = 10;

        List<Entity> layers = new ArrayList<>();

        layers.add(new SimpleEntityBuilder(this)
                .positionAt(0, 0)
                .addView(new ScrollingBackgroundView(getAssetLoader().loadTexture("background/bushes.png", 1280, 720),
                        Orientation.HORIZONTAL, 1.20))
                .layerAt(++foregroundParallaxIndex)
                .buildEntity());

        layers.add(new SimpleEntityBuilder(this)
                .positionAt(0, 0)
                .addView(new ScrollingBackgroundView(getAssetLoader().loadTexture("background/trail.png", 1280, 720),
                        Orientation.HORIZONTAL, 1.00))
                .layerAt(--backgroundParallaxIndex)
                .buildEntity());

        layers.add(new SimpleEntityBuilder(this)
                .positionAt(0, 0)
                .addView(new ScrollingBackgroundView(getAssetLoader().loadTexture("background/trees_foreground.png", 1280, 720),
                        Orientation.HORIZONTAL, 0.90))
                .layerAt(--backgroundParallaxIndex)
                .buildEntity());

        layers.add(new SimpleEntityBuilder(this)
                .positionAt(0, 0)
                .addView(new ScrollingBackgroundView(getAssetLoader().loadTexture("background/trees_background.png", 1280, 720),
                        Orientation.HORIZONTAL, 0.70))
                .layerAt(--backgroundParallaxIndex)
                .buildEntity());

        layers.add(new SimpleEntityBuilder(this)
                .positionAt(0, 0)
                .addView(new ScrollingBackgroundView(getAssetLoader().loadTexture("background/background_texture.png", 1280, 720),
                        Orientation.HORIZONTAL, 0.50))
                .layerAt(--backgroundParallaxIndex)
                .buildEntity());

        layers.forEach(level.getEntities()::add);

        List<Entity> borders = new ArrayList<>();

        borders.add(new SimpleEntityBuilder(this)
                .positionAt(0, 0)
                .addHitBox(new HitBox(BoundingShape.box(1, level.getHeight())))
                .attachComponents(new PhysicsComponent())
                .buildEntity());

        borders.add(new SimpleEntityBuilder(this)
                .positionAt(level.getWidth(), 0)
                .addHitBox(new HitBox(BoundingShape.box(1, level.getHeight())))
                .attachComponents(new PhysicsComponent())
                .buildEntity());

        borders.forEach(level.getEntities()::add);

        return level;
    }
}