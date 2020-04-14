package org.monjasa.engine.entities.factories;

import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.Level;
import org.monjasa.engine.entities.exits.Exit;
import org.monjasa.engine.entities.platforms.Platform;
import org.monjasa.engine.entities.players.Player;

public interface PlatformerEntityFactory extends EntityFactory {

    Platform getPlatformInstance();

    Platform createPlatform(SpawnData data);

    Player getPlayerInstance();

    Player createPlayer(SpawnData data);

    Exit getExitInstance();

    Exit createExit(SpawnData data);

    int getMaxLevel();

    Level createLevel(int levelNum, boolean isDevelopingNewLevel);
}
