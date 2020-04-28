package org.monjasa.engine.entities.checkpoints;

import org.monjasa.engine.entities.PlatformerEntityBuilder;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;

public class CheckpointBuilder extends PlatformerEntityBuilder<CheckpointBuilder, Checkpoint> {

    public CheckpointBuilder(PlatformerLevelFactory factory) {
        super(factory);
        entity = factory.getCheckpointInstance();
    }

    @Override
    protected CheckpointBuilder getThis() {
        return this;
    }

    @Override
    public CheckpointBuilder resetEntity() {
        entity = factory.getCheckpointInstance();
        return this;
    }

    public Checkpoint buildCheckpoint() {
        return entity;
    }
}
