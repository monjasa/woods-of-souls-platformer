package org.monjasa.engine.perks;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.core.serialization.SerializableType;
import com.almasb.fxgl.entity.Entity;
import org.monjasa.engine.entities.PlatformerEntityType;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PerkTree implements SerializableType {

    private Map<Class<? extends Perk>, Perk> perks;
    private Deque<Perk> perksDeque;

    private Map<Class<? extends Perk>, Integer> perksHistory;
    private Deque<Perk> perksToUndo;

    public PerkTree() {

        perks = new HashMap<>();
        perksDeque = new ArrayDeque<>();

        perksHistory = new HashMap<>();
        perksToUndo = new ArrayDeque<>();

        putPerk(new HPChangingPerk(10, 1));
        putPerk(new SpeedChangingPerk(25, 2));
    }

    @Override
    public void read(Bundle bundle) {

        Entity player = getGameWorld().getSingleton(PlatformerEntityType.PLAYER);

        perksHistory = new HashMap<>();

        perks.forEach((perkClass, perk) -> {
            if (bundle.exists(perkClass.getSimpleName())) {
                for (int i = 0; i < bundle.<Integer>get(perkClass.getSimpleName()); i++) {
                    perk.execute(player);
                    perksHistory.merge(perkClass, 1, Integer::sum);
                }
            }
        });
    }

    @Override
    public void write(Bundle bundle) {
        perksHistory.forEach((perk, value) -> bundle.put(perk.getSimpleName(), value));
    }

    public void putPerk(Perk perk) {
        perks.put(perk.getClass(), perk);
    }

    public void executePerk(Class<? extends Perk> perkClass) {
        Perk perkToExecute = perks.get(perkClass);
        if (perkToExecute.execute(getGameWorld().getSingleton(PlatformerEntityType.PLAYER))) {
            perksDeque.push(perkToExecute);
        }
    }

    public void undoLastPerk() {
        if (!perksDeque.isEmpty())
            perksDeque.pop().undo(getGameWorld().getSingleton(PlatformerEntityType.PLAYER));
    }

    public void undoPerks() {
        perksToUndo.forEach(perk -> perk.undo(getGameWorld().getSingleton(PlatformerEntityType.PLAYER)));
        perksToUndo.clear();
    }

    public void closePerkTree() {
        perksToUndo.addAll(perksDeque);
        perksDeque.clear();
    }

    public void savePerkTree() {

        perksToUndo.stream()
                .map(Perk::getClass)
                .forEach(perkClass -> perksHistory.merge(perkClass, 1, Integer::sum));

        perksToUndo.clear();
    }

    public Map<Class<? extends Perk>, Integer> getPerksHistory() {
        return perksHistory;
    }
}
