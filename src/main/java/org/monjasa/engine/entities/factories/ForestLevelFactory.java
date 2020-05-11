package org.monjasa.engine.entities.factories;

import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
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
import org.monjasa.engine.entities.checkpoints.Checkpoint;
import org.monjasa.engine.entities.checkpoints.CheckpointBuilder;
import org.monjasa.engine.entities.checkpoints.ForestCheckpoint;
import org.monjasa.engine.entities.coins.Coin;
import org.monjasa.engine.entities.coins.CoinBuilder;
import org.monjasa.engine.entities.coins.CoinFlyweightFactory;
import org.monjasa.engine.entities.coins.ForestCoin;
import org.monjasa.engine.entities.components.DynamicComponent;
import org.monjasa.engine.entities.components.EntityHPComponent;
import org.monjasa.engine.entities.enemies.Enemy;
import org.monjasa.engine.entities.enemies.EnemyBuilder;
import org.monjasa.engine.entities.enemies.ForestEnemy;
import org.monjasa.engine.entities.exits.Exit;
import org.monjasa.engine.entities.exits.ExitBuilder;
import org.monjasa.engine.entities.exits.ForestExit;
import org.monjasa.engine.entities.platforms.ForestPlatform;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.platforms.PlatformBuilder;
import org.monjasa.engine.entities.players.ForestPlayer;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.entities.players.PlayerBuilder;
import org.monjasa.engine.entities.players.components.ForestPlayerControlComponent;
import org.monjasa.engine.entities.players.components.ForestPlayerViewComponent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ForestLevelFactory extends PlatformerLevelFactory {

    private static final String FOREST_LEVEL_PREFIX = "forest";
    private static final String FOREST_DEVELOPING_LEVEL_NAME = "level_dev";

    private CoinFlyweightFactory coinFactory = CoinFlyweightFactory.getCoinFactoryInstance();

    public ForestLevelFactory(int maxLevel) {

        super(maxLevel, FOREST_LEVEL_PREFIX, FOREST_DEVELOPING_LEVEL_NAME);

        coinSpritesheetName = "forest-coin-spritesheet-min.png";
        coinCollectSoundName = "pickup-coin.wav";

        playerHorizontalVelocity = 200;
        playerVerticalVelocity = 800;

        playerComponents = List.of(
                new DynamicComponent(playerHorizontalVelocity, playerVerticalVelocity),
                new ForestPlayerViewComponent(),
                new ForestPlayerControlComponent(),
                new EntityHPComponent(100)
        );

        enemyDamage = 30;
    }

    @Override
    public Platform getPlatformInstance() {
        return new ForestPlatform();
    }

    @Override
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
    public Player createPlayer(SpawnData data) {

        PhysicsComponent playerPhysicsComponent = new PhysicsComponent();
        playerPhysicsComponent.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(25, 119),
                BoundingShape.box(40, 2)));
        playerPhysicsComponent.setBodyType(BodyType.DYNAMIC);
        playerPhysicsComponent.setFixtureDef(new FixtureDef().friction(0.0f));

//        HitBox playerMainHitBox = new HitBox("PLAYER_HITBOX",
//                new Point2D(20, 0),
//                BoundingShape.box(50, 165));

        Player player = new PlayerBuilder(this)
                .loadFromSpawnData(data)
                .addType(PlatformerEntityType.PLAYER)
                .centerAt(data.<Integer>get("width") / 2.0, data.<Integer>get("height") / 2.0)
//                .addHitBox(playerMainHitBox)
                .addHitBox(new HitBox(BoundingShape.box(80, 120)))
                .layerAt(1)
                .attachComponents(playerPhysicsComponent)
                .attachComponents(playerComponents)
                .setCollidable()
                .buildPlayer();

        return player;
    }

    @Override
    public Enemy getEnemyInstance() {
        return new ForestEnemy(enemyDamage);
    }

    @Override
    public Enemy createEnemy(SpawnData data) {

        int patrolEndX = data.get("patrolEndX");

        return new EnemyBuilder(this)
                .addType(PlatformerEntityType.ENEMY)
                .loadFromSpawnData(data)
                .layerAt(1)
                .attachComponents(new ForestEnemy.ForestEnemyComponent(patrolEndX))
                .addHitBox(new HitBox(BoundingShape.box(75, 99)))
                .setCollidable()
                .buildEnemy();
    }

    @Override
    public Exit getExitInstance() {
        return new ForestExit();
    }

    @Override
    public Exit createExit(SpawnData data) {

        return new ExitBuilder(this)
                .loadFromSpawnData(data)
                .addType(PlatformerEntityType.EXIT)
                .addHitBox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .setCollidable()
                .buildExit();
    }

    @Override
    public Coin getCoinInstance() {
        return new ForestCoin(coinFactory.getCoinFlyweight(this));
    }

    @Override
    public Coin createCoin(SpawnData data) {

        return new CoinBuilder(this)
                .loadFromSpawnData(data)
                .addType(PlatformerEntityType.COIN)
                .addHitBox(new HitBox(BoundingShape.circle(9)))
                .attachComponents(new ForestCoin.ForestCoinViewComponent())
                .setCollidable()
                .buildCoin();
    }

    @Override
    public Checkpoint getCheckpointInstance() {
        return new ForestCheckpoint();
    }

    @Override
    public Checkpoint createCheckpoint(SpawnData data) {
        return new CheckpointBuilder(this)
                .loadFromSpawnData(data)
                .addType(PlatformerEntityType.CHECKPOINT)
                .layerAt(0)
                .addView(texture("stone-checkpoint.png", 105, 224))
                .addHitBox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .setCollidable()
                .buildCheckpoint();
    }

    @Override
    protected List<Entity> createBackground() {

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
                .addView(new ScrollingBackgroundView(getAssetLoader().loadTexture("background/trees-foreground.png", 1280, 720),
                        Orientation.HORIZONTAL, 0.90))
                .layerAt(--backgroundParallaxIndex)
                .buildEntity());

        layers.add(new SimpleEntityBuilder(this)
                .positionAt(0, 0)
                .addView(new ScrollingBackgroundView(getAssetLoader().loadTexture("background/trees-background.png", 1280, 720),
                        Orientation.HORIZONTAL, 0.70))
                .layerAt(--backgroundParallaxIndex)
                .buildEntity());

        layers.add(new SimpleEntityBuilder(this)
                .positionAt(0, 0)
                .addView(new ScrollingBackgroundView(getAssetLoader().loadTexture("background/background-texture.png", 1280, 720),
                        Orientation.HORIZONTAL, 0.50))
                .layerAt(--backgroundParallaxIndex)
                .buildEntity());

        return layers;
    }
}