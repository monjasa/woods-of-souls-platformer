package org.monjasa.engine.entities.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import org.monjasa.engine.entities.SimpleEntityBuilder;
import org.monjasa.engine.entities.checkpoints.Checkpoint;
import org.monjasa.engine.entities.coins.Coin;
import org.monjasa.engine.entities.enemies.Enemy;
import org.monjasa.engine.entities.exits.Exit;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.levels.tmx.PlatformerTMXLoaderFacade;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;

public abstract class PlatformerLevelFactory {

    protected int maxLevel;
    protected String levelPrefix;
    protected String developingLevelName;

    protected String coinSpritesheetName;
    protected String coinCollectSoundName;

    protected List<Component> playerComponents;
    protected double playerHorizontalVelocity;
    protected double playerVerticalVelocity;

    protected int enemyDamage;

    public PlatformerLevelFactory(int maxLevel, String levelPrefix, String developingLevelName) {
        this.maxLevel = maxLevel;
        this.levelPrefix = levelPrefix;
        this.developingLevelName = developingLevelName;
    }

    public abstract Platform getPlatformInstance();

    public abstract Platform createPlatform(SpawnData data);

    public abstract Player getPlayerInstance();

    public abstract Player createPlayer(SpawnData data);

    public abstract Enemy getEnemyInstance();

    public abstract Enemy createEnemy(SpawnData data);

    public abstract Coin getCoinInstance();

    public abstract Coin createCoin(SpawnData data);

    public abstract Exit getExitInstance();

    public abstract Exit createExit(SpawnData data);

    public abstract Checkpoint getCheckpointInstance();

    public abstract Checkpoint createCheckpoint(SpawnData data);

    public final Level createLevel(URL levelURL, boolean isDevelopingNewLevel) {

        Level level = loadLevelFromTMX(levelURL, isDevelopingNewLevel);

        List<Entity> layers = createBackground();
        layers.forEach(level.getEntities()::add);

        List<Entity> borders = createBorders(level.getWidth(), level.getHeight());
        borders.forEach(level.getEntities()::add);

        return level;
    }

    private Level loadLevelFromTMX(URL levelURL, boolean isDevelopingNewLevel) {

        if (isDevelopingNewLevel && developingLevelName != null) {
            return getAssetLoader().loadLevel(String.format("tmx/%s.tmx", developingLevelName),
                    new PlatformerTMXLoaderFacade());
        } else {
            return new PlatformerTMXLoaderFacade().load(levelURL, FXGL.getGameWorld());
        }
    }

    protected abstract List<Entity> createBackground();

    protected List<Entity> createBorders(double levelWidth, double levelHeight) {

        List<Entity> borders = new ArrayList<>();

        borders.add(new SimpleEntityBuilder(this)
                .positionAt(0, 0)
                .addHitBox(new HitBox(BoundingShape.box(1, levelHeight)))
                .attachComponents(new PhysicsComponent())
                .buildEntity());

        borders.add(new SimpleEntityBuilder(this)
                .positionAt(levelWidth, 0)
                .addHitBox(new HitBox(BoundingShape.box(1, levelHeight)))
                .attachComponents(new PhysicsComponent())
                .buildEntity());

        return borders;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getCoinSpritesheetName() {
        return coinSpritesheetName;
    }

    public String getCoinCollectSoundName() {
        return coinCollectSoundName;
    }

    public String getLevelPrefix() {
        return levelPrefix;
    }
}
