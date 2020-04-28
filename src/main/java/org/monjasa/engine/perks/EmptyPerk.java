package org.monjasa.engine.perks;

public class EmptyPerk implements Perk {

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public void undo() {
    }
}
