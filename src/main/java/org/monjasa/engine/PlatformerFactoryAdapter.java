package org.monjasa.engine;

import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.level.Level;
import org.monjasa.engine.entities.PlatformerEntityFactory;
import org.monjasa.engine.entities.checkpoints.Checkpoint;
import org.monjasa.engine.entities.coins.Coin;
import org.monjasa.engine.entities.enemies.Enemy;
import org.monjasa.engine.entities.exits.Exit;
import org.monjasa.engine.entities.factories.ForestLevelFactory;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.players.Player;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlatformerFactoryAdapter implements PlatformerEntityFactory {

    private PlatformerLevelFactory currentFactory;
    private Map<String, PlatformerLevelFactory> factories;

    public PlatformerFactoryAdapter() {

        factories = new LinkedHashMap<>();
        factories.put("forest", new ForestLevelFactory(5));
    }

    @Override
    @Spawns("platform")
    public Platform spawnPlatform(SpawnData data) {
        assert currentFactory != null;
        return currentFactory.createPlatform(data);
    }

    @Override
    @Spawns("player")
    public Player spawnPlayer(SpawnData data) {
        assert currentFactory != null;
        return currentFactory.createPlayer(data);
    }

    @Override
    @Spawns("enemy")
    public Enemy spawnEnemy(SpawnData data) {
        assert currentFactory != null;
        return currentFactory.createEnemy(data);
    }

    @Override
    @Spawns("exit")
    public Exit spawnExit(SpawnData data) {
        assert currentFactory != null;
        return currentFactory.createExit(data);
    }

    @Override
    @Spawns("coin")
    public Coin spawnCoin(SpawnData data) {
        assert currentFactory != null;
        return currentFactory.createCoin(data);
    }

    @Override
    @Spawns("checkpoint")
    public Checkpoint spawnCheckpoint(SpawnData data) {
        assert currentFactory != null;
        return currentFactory.createCheckpoint(data);
    }

    @Override
    public Level createLevel(URL levelURL, boolean isDevelopingNewLevel) {

        String levelName = new File(levelURL.getPath()).getName();
        Pattern pattern = Pattern.compile("([^_]+)");
        Matcher matcher = pattern.matcher(levelName);

        if (!matcher.find()) throw new RuntimeException();

        currentFactory = factories.get(levelName.substring(matcher.start(), matcher.end()));

        assert currentFactory != null;
        return currentFactory.createLevel(levelURL, isDevelopingNewLevel);
    }

    public PlatformerLevelFactory getCurrentFactory() {
        return currentFactory;
    }

    @Override
    public List<PlatformerLevelFactory> getLevelFactories() {
        return new ArrayList<>(factories.values());
    }
}
