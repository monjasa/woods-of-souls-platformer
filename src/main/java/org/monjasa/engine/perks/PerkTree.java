package org.monjasa.engine.perks;

import org.monjasa.engine.entities.PlatformerEntityType;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

public class PerkTree {

    private Map<Class<? extends Perk>, Perk> perkList;
    private Deque<Perk> perkDeque;

    public PerkTree() {

        perkList = new HashMap<>();

        perkList.put(
                HPChangingPerk.class,
                new HPChangingPerk(getGameWorld().getSingleton(PlatformerEntityType.PLAYER), 10, 1)
        );

        perkList.put(
                SpeedChangingPerk.class,
                new SpeedChangingPerk(getGameWorld().getSingleton(PlatformerEntityType.PLAYER), 0.1, 2)
        );

        perkDeque = new ArrayDeque<>();
    }

    public void executePerk(Class<? extends Perk> perkClass) {
        Perk perkToExecute = perkList.get(perkClass);
        if (perkToExecute.execute()) perkDeque.push(perkToExecute);
    }

    public void undoLastPerk() {
        if (!perkDeque.isEmpty())
            perkDeque.pop().undo();
    }

    public void clearPerkDeque() {
        perkDeque.clear();
    }
}
