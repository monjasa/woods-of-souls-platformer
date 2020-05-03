package org.monjasa.engine.entities;

import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.level.Level;
import org.monjasa.engine.entities.checkpoints.Checkpoint;
import org.monjasa.engine.entities.coins.Coin;
import org.monjasa.engine.entities.enemies.Enemy;
import org.monjasa.engine.entities.exits.Exit;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.players.Player;

import java.net.URL;
import java.util.Deque;
import java.util.List;

public interface PlatformerEntityFactory extends EntityFactory {

    @Spawns("platform")
    Platform spawnPlatform(SpawnData data);

    @Spawns("player")
    Player spawnPlayer(SpawnData data);

    @Spawns("enemy")
    Enemy spawnEnemy(SpawnData data);

    @Spawns("exit")
    Exit spawnExit(SpawnData data);

    @Spawns("coin")
    Coin spawnCoin(SpawnData data);

    @Spawns("checkpoint")
    Checkpoint spawnCheckpoint(SpawnData data);

    Level createLevel(URL levelURL, boolean isDevelopingNewLevel);

    PlatformerLevelFactory getCurrentFactory();

    List<PlatformerLevelFactory> getLevelFactories();
}
