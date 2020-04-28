package org.monjasa.engine.levels;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import org.monjasa.engine.entities.PlatformerEntityFactory;
import org.monjasa.engine.entities.PlatformerEntityType;
import org.monjasa.engine.entities.components.EntityHPComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlatformerLevel {

    private Level level;
    private Map<String, Object> playerState;
    private IntegerProperty collectedCoinsProperty;
    private List<Entity> coinsToRestore;

    public PlatformerLevel(PlatformerEntityFactory entityFactory, boolean developing) {
        playerState = new HashMap<>();
        coinsToRestore = new ArrayList<>();

        level = entityFactory.peekCurrentLevelFactory().createLevel(geti("level"), developing);

        collectedCoinsProperty = new SimpleIntegerProperty();
        collectedCoinsProperty.bind(getWorldProperties().intProperty("coins-total-collected"));
    }

    public LevelMemento onCheckpoint() {
        coinsToRestore.clear();
        return makeSnapshot();
    }

    public void addCoinToRestore(Entity coin) {
        coinsToRestore.add(coin);
    }

    public LevelMemento makeSnapshot() {

        Entity player = level.getEntities().stream()
                .filter(entity -> entity.getType() == PlatformerEntityType.PLAYER)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        playerState.put("position", player.getTransformComponent().getPosition());
        playerState.put("health", player.getComponent(EntityHPComponent.class).getValue());

        return new LevelMemento(playerState, collectedCoinsProperty.getValue());
    }

    public void restoreLevel(LevelMemento levelSnapshot) {

        Entity player = getGameWorld().getSingleton(PlatformerEntityType.PLAYER);

        player.getComponent(PhysicsComponent.class)
                .overwritePosition((Point2D) levelSnapshot.playerState.get("position"));

        player.getComponent(EntityHPComponent.class).setValue((int) levelSnapshot.playerState.get("health"));

        getWorldProperties().setValue("coins-total-collected", levelSnapshot.collectedCoins);
    }

    public Level getLevel() {
        return level;
    }

    public List<Entity> getCoinsToRestore() {
        return coinsToRestore;
    }

    public static class LevelMemento {

        private Map<String, Object> playerState;
        int collectedCoins;

        private LevelMemento(Map<String, Object> playerState, int collectedCoins) {
            this.playerState = Map.copyOf(playerState);
            this.collectedCoins = collectedCoins;
        }
    }
}
