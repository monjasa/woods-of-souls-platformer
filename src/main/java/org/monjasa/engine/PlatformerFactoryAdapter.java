package org.monjasa.engine;

import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.level.Level;
import org.monjasa.engine.entities.coins.Coin;
import org.monjasa.engine.entities.enemies.Enemy;
import org.monjasa.engine.entities.exits.Exit;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.players.Player;

import java.util.Deque;

public class PlatformerFactoryAdapter implements EntityFactory {

    private Deque<PlatformerLevelFactory> entityFactories;

    public PlatformerFactoryAdapter(Deque<PlatformerLevelFactory> entityFactories) {
        this.entityFactories = entityFactories;
    }

    @Spawns("platform")
    public Platform createPlatform(SpawnData data) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createPlatform(data);
    }

    @Spawns("player")
    public Player createPlayer(SpawnData data) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createPlayer(data);
    }

    @Spawns("enemy")
    public Enemy createEnemy(SpawnData data) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createEnemy(data);
    }

    @Spawns("exit")
    public Exit createExit(SpawnData data) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createExit(data);
    }

    @Spawns("coin")
    public Coin createCoin(SpawnData data) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createCoin(data);
    }

    public Level createLevel(int levelNum, boolean isDevelopingNewLevel) {
        assert entityFactories.peek() != null;
        return entityFactories.peek().createLevel(levelNum, isDevelopingNewLevel);
    }

    public PlatformerLevelFactory peekCurrentLevelFactory() {
        return entityFactories.peek();
    }

    public void pollCurrentLevelFactory() {
        entityFactories.poll();
    }

    public boolean isEmpty() {
        return entityFactories.isEmpty();
    }
}
