package org.monjasa.engine.perks;

import org.monjasa.engine.entities.PlatformerEntityType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

public class PerkTree {

    private List<Perk> perkList;
    private Deque<Perk> perkDeque;

    public PerkTree() {

        perkList = new LinkedList<>();
        perkList.add(new HPChangingPerk(getGameWorld().getSingleton(PlatformerEntityType.PLAYER), 10, 1));
        perkList.add(new SpeedChangingPerk(getGameWorld().getSingleton(PlatformerEntityType.PLAYER), 0.1, 2));

        perkDeque = new ArrayDeque<>();
    }

    public void executePerk(int index) {
        Perk perkToExecute = perkList.get(index);
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
