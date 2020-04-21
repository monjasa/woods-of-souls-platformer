package org.monjasa.engine.entities.factories;

import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import org.monjasa.engine.entities.coins.Coin;
import org.monjasa.engine.entities.enemies.Enemy;
import org.monjasa.engine.entities.exits.Exit;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.players.Player;
import org.monjasa.engine.levels.PlatformerTMXLevelLoader;

import java.net.URL;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;

public abstract class PlatformerLevelFactory implements EntityFactory {

    protected int maxLevel;
    protected String levelPrefix;
    protected String developingLevelName;

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

    public Level createLevel(int levelNum, boolean isDevelopingNewLevel) {

        if (isDevelopingNewLevel && developingLevelName != null) {
            return getAssetLoader().loadLevel(String.format("tmx/%s.tmx", developingLevelName), new PlatformerTMXLevelLoader());
        } else {
            return getAssetLoader().loadLevel(String.format("tmx/%s_%02d.tmx", levelPrefix, levelNum), new PlatformerTMXLevelLoader());
        }
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
