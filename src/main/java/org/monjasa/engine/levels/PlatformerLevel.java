package org.monjasa.engine.levels;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import org.monjasa.engine.entities.PlatformerEntityType;
import org.monjasa.engine.entities.components.EntityHPComponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getWorldProperties;

public class PlatformerLevel {

    private Level level;
    private IntegerProperty coinsCollectedProperty;
    private IntegerProperty coinsAvailableProperty;

    //    private List<Perk> perksToUndo;
//    private List<Perk> executedPerks;
    private List<Entity> coinsToRestore;

    public PlatformerLevel(Level level) {

//        perksToUndo = new ArrayList<>();
//        executedPerks = new ArrayList<>();
        coinsToRestore = new ArrayList<>();

//        level = entityFactory.peekCurrentLevelFactory().createLevel(geti("level"), developing);
        this.level = level;

        coinsCollectedProperty = new SimpleIntegerProperty();
        coinsAvailableProperty = new SimpleIntegerProperty();
        coinsCollectedProperty.bind(getWorldProperties().intProperty("coinsCollected"));
        coinsAvailableProperty.bind(getWorldProperties().intProperty("coinsAvailable"));
    }

    public LevelMemento onCheckpoint() {
//        executedPerks.addAll(perksToUndo);
//        perksToUndo.clear();
        coinsToRestore.clear();
        return makeSnapshot();
    }

//    public void addPerkToUndo(Perk perk) {
//        perksToUndo.add(perk);
//    }

    public void addCoinToRestore(Entity coin) {
        coinsToRestore.add(coin);
    }

    public LevelMemento makeSnapshot() {

        Entity player = level.getEntities().stream()
                .filter(entity -> entity.getType() == PlatformerEntityType.PLAYER)
                .findFirst()
                .orElseThrow(NoSuchEntityException::new);

        return new LevelMementoBuilder()
                .addProperty("coinsCollected", coinsCollectedProperty.getValue())
                .addProperty("coinsAvailable", coinsAvailableProperty.getValue())
//                .addProperty("perks", executedPerks)
                .addEntityProperties(player, EntityHPComponent.class)
                .buildMemento();
    }

    public void restoreLevel(LevelMemento levelSnapshot) {

        Entity player = getGameWorld().getSingleton(PlatformerEntityType.PLAYER);

//        perksToUndo.forEach(perk -> perk.undo(player));
//        perksToUndo.clear();

        player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(
                levelSnapshot.<Double>getProperty("position.x"),
                levelSnapshot.<Double>getProperty("position.y")
        ));

        player.getComponent(EntityHPComponent.class).read(levelSnapshot.mementoBundle);

        getWorldProperties().setValue("coinsCollected", levelSnapshot.<Integer>getProperty("coinsCollected"));
        getWorldProperties().setValue("coinsAvailable", levelSnapshot.<Integer>getProperty("coinsAvailable"));

        for (Entity coin : coinsToRestore) {
            coin.addComponent(new CollidableComponent(true));
            coin.setVisible(true);
        }
        coinsToRestore.clear();
    }

    public Level getLevel() {
        return level;
    }

    public List<Entity> getCoinsToRestore() {
        return coinsToRestore;
    }

    public static class LevelMementoBuilder {

        private LevelMemento levelMemento;

        LevelMementoBuilder() {
            levelMemento = new LevelMemento();
        }

        <T extends Serializable> LevelMementoBuilder addProperty(String key, T value) {
            levelMemento.mementoBundle.put(key, value);
            return this;
        }

        LevelMementoBuilder addProperties(Map<String, Serializable> properties) {
            properties.forEach(levelMemento.mementoBundle::put);
            return this;
        }

        @SafeVarargs
        final <E extends Component & SerializableComponent>
        LevelMementoBuilder addEntityProperties(Entity entity, Class<E>... componentClasses) {

            levelMemento.mementoBundle.put("position.x", entity.getTransformComponent().getPosition().getX());
            levelMemento.mementoBundle.put("position.y", entity.getTransformComponent().getPosition().getY());

            Stream.of(componentClasses)
                    .map(entity::getComponent)
                    .forEach(component -> component.write(levelMemento.mementoBundle));

            return this;
        }

        LevelMementoBuilder reset() {
            levelMemento = new LevelMemento();
            return this;
        }

        LevelMemento buildMemento() {
            System.out.println(levelMemento);
            return levelMemento;
        }
    }

    public static class LevelMemento {

        private Bundle mementoBundle;

        LevelMemento() {
            mementoBundle = new Bundle("Memento");
        }

        public <T extends Serializable> T getProperty(String key) {
            return mementoBundle.<T>get(key);
        }

        @Override
        public String toString() {
            return "LevelMemento{" +
                    "mementoBundle=" + mementoBundle +
                    '}';
        }
    }
}
