package org.monjasa.engine.entities.players;

import org.monjasa.engine.entities.players.components.ForestPlayerControlComponent;
import org.monjasa.engine.entities.players.components.ForestPlayerViewComponent;

public class ForestPlayer extends Player {

    @Override
    public Player attachPlayerComponents() {
        playerViewComponent = getComponent(ForestPlayerViewComponent.class);
        playerControlComponent = getComponent(ForestPlayerControlComponent.class);
        return this;
    }
}
