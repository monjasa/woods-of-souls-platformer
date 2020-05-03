package org.monjasa.engine.perks;

import com.almasb.fxgl.dsl.FXGL;
import org.monjasa.engine.PlatformerApplication;
import org.monjasa.engine.entities.PlatformerEntityType;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PerkTree {

    private Map<Class<? extends Perk>, Perk> perks;
    private Deque<Perk> perksDeque;

    public PerkTree() {

        perks = new HashMap<>();

        perks.put(
                HPChangingPerk.class,
                new HPChangingPerk(10, 1)
        );

        perks.put(
                SpeedChangingPerk.class,
                new SpeedChangingPerk(25, 2)
        );

        perksDeque = new ArrayDeque<>();
    }

    public void executePerk(Class<? extends Perk> perkClass) {
        Perk perkToExecute = perks.get(perkClass);
//        FXGL.<PlatformerApplication>getAppCast().getCurrentLevel().addPerkToUndo(perkToExecute);
        if (perkToExecute.execute(getGameWorld().getSingleton(PlatformerEntityType.PLAYER))) perksDeque.push(perkToExecute);
    }

    public void undoLastPerk() {
        if (!perksDeque.isEmpty())
            perksDeque.pop().undo(getGameWorld().getSingleton(PlatformerEntityType.PLAYER));
    }

    public void clearPerkDeque() {
        perksDeque.clear();
    }
}
