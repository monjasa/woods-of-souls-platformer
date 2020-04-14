package org.monjasa.engine.entities.platforms;

import org.monjasa.engine.entities.PlatformerEntityBuilder;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;

public class PlatformBuilder extends PlatformerEntityBuilder<PlatformBuilder, Platform> {

    public PlatformBuilder(PlatformerLevelFactory factory) {
        super(factory);
        entity = factory.getPlatformInstance();
    }

    @Override
    protected PlatformBuilder getThis() {
        return this;
    }

    @Override
    public PlatformBuilder resetEntity() {
        entity = factory.getPlatformInstance();
        return this;
    }

    public Platform buildPlatform() {
        return entity;
    }
}
