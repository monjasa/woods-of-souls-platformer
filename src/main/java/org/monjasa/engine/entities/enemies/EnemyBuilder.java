package org.monjasa.engine.entities.enemies;

import org.monjasa.engine.entities.PlatformerEntityBuilder;
import org.monjasa.engine.entities.factories.PlatformerLevelFactory;

public class EnemyBuilder extends PlatformerEntityBuilder<EnemyBuilder, Enemy> {

    public EnemyBuilder(PlatformerLevelFactory factory) {
        super(factory);
        entity = factory.getEnemyInstance();
    }

    @Override
    protected EnemyBuilder getThis() {
        return this;
    }

    @Override
    public EnemyBuilder resetEntity() {
        entity = factory.getEnemyInstance();
        return this;
    }

    public Enemy buildEnemy() {
        return entity;
    }
}