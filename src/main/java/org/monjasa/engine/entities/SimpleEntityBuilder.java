package org.monjasa.engine.entities;

import com.almasb.fxgl.entity.Entity;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;

public class SimpleEntityBuilder extends PlatformerEntityBuilder<SimpleEntityBuilder, Entity> {

    public SimpleEntityBuilder(PlatformerLevelFactory factory) {
        super(factory);
        entity = new Entity();
    }

    @Override
    protected SimpleEntityBuilder getThis() {
        return this;
    }

    @Override
    public SimpleEntityBuilder resetEntity() {
        return this;
    }

    public Entity buildEntity() {
        return entity;
    }
}