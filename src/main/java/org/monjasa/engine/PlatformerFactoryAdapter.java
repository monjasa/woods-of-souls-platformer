package org.monjasa.engine;

import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.level.Level;
import org.monjasa.engine.entities.PlatformerEntityFactory;
import org.monjasa.engine.entities.coins.Coin;
import org.monjasa.engine.entities.enemies.Enemy;
import org.monjasa.engine.entities.exits.Exit;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.players.Player;

import java.util.Deque;

public class PlatformerFactoryAdapter implements PlatformerEntityFactory {

    private Deque<PlatformerLevelFactory> entityFactories;

    public PlatformerFactoryAdapter(Deque<PlatformerLevelFactory> entityFactories) {
        this.entityFactories = entityFactories;
    }

    @Override
    @Spawns("platform")
    public Platform spawnPlatform(SpawnData data) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createPlatform(data);
    }

    @Override
    @Spawns("player")
    public Player spawnPlayer(SpawnData data) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createPlayer(data);
    }

    @Override
    @Spawns("enemy")
    public Enemy spawnEnemy(SpawnData data) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createEnemy(data);
    }

    @Override
    @Spawns("exit")
    public Exit spawnExit(SpawnData data) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createExit(data);
    }

    @Override
    @Spawns("coin")
    public Coin spawnCoin(SpawnData data) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createCoin(data);
    }

    @Override
    public Level createLevel(int levelNum, boolean isDevelopingNewLevel) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createLevel(levelNum, isDevelopingNewLevel);
    }

    @Override
    public PlatformerLevelFactory peekCurrentLevelFactory() {
        return entityFactories.peek();
    }

    @Override
    public void pollCurrentLevelFactory() {
        entityFactories.poll();
    }

    @Override
    public boolean isEmpty() {
        return entityFactories.isEmpty();
    }
}
