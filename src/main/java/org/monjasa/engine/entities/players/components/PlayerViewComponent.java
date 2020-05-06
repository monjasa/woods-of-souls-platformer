package org.monjasa.engine.entities.players.components;

import com.almasb.fxgl.entity.component.Component;

public abstract class PlayerViewComponent extends Component {

    public abstract void onMovingHorizontally();

    public abstract void onHorizontalStop();

    public abstract void onVerticalStart();

    public abstract void onMovingVertically();

    public abstract void onVerticalStop();

    public abstract void onMeleeAttack();

    public abstract void onRangedAttack();
}
