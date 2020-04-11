package org.monjasa.engine.entities.factories;

import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.players.Player;

public interface PlatformerEntityFactory extends EntityFactory {

    Platform getPlatformInstance();

    Platform createPlatform(SpawnData data);

    Player getPlayerInstance();

    Player createPlayer(SpawnData data);
}
