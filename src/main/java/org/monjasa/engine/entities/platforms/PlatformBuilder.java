package org.monjasa.engine.entities.platforms;

import org.monjasa.engine.entities.builder.EntityBuilder;
import org.monjasa.engine.entities.builder.PlatformerEntityBuilder;
import org.monjasa.engine.entities.factories.PlatformerEntityFactory;

public class PlatformBuilder extends PlatformerEntityBuilder {

    public PlatformBuilder(PlatformerEntityFactory factory) {
        super(factory);
        entity = factory.getPlatformInstance();
    }

    @Override
    public EntityBuilder resetEntity() {
        entity = factory.getPlatformInstance();
        return this;
    }
}
