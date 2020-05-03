package org.monjasa.engine.levels;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import org.monjasa.engine.entities.PlatformerEntityType;
import org.monjasa.engine.perks.HPChangingPerk;
import org.monjasa.engine.perks.SpeedChangingPerk;

import static com.almasb.fxgl.dsl.FXGL.*;

public class LevelSaveLoadHandler implements SaveLoadHandler {

    @Override
    public void onLoad(DataFile dataFile) {

        Bundle levelBundle = dataFile.getBundle("Level");
        getWorldProperties().setValue("level", levelBundle.<Integer>get("level"));
        getWorldProperties().setValue("coinsCollected", levelBundle.<Integer>get("coinsCollected"));
        getWorldProperties().setValue("coinsAvailable", levelBundle.<Integer>get("coinsCollected"));

        Bundle perksBundle = dataFile.getBundle("Perks");

        HPChangingPerk hpChangingPerk = perksBundle.get(HPChangingPerk.class.getSimpleName());
//        hpChangingPerk.setReceiver(FXGL.getGameWorld().getSingleton(PlatformerEntityType.PLAYER));
        hpChangingPerk.execute(getGameWorld().getSingleton(PlatformerEntityType.PLAYER));
    }

    @Override
    public void onSave(DataFile dataFile) {

        Bundle levelBundle = new Bundle("Level");
        levelBundle.put("level", geti("level"));
        levelBundle.put("coinsCollected", geti("coinsCollected"));

        Bundle perksBundle = new Bundle("Perks");
        perksBundle.put(HPChangingPerk.class.getSimpleName(), new HPChangingPerk(10, 1));
        perksBundle.put(SpeedChangingPerk.class.getSimpleName(), new SpeedChangingPerk(0.25, 2));

        Bundle fxglServicesBundle = new Bundle("FXGLServices");
        fxglServicesBundle.put("globalSoundVolume", 0.5);
        fxglServicesBundle.put("fullscreen", false);
        fxglServicesBundle.put("globalMusicVolume", 0.5);

        dataFile.putBundle(levelBundle);
        dataFile.putBundle(perksBundle);
        dataFile.putBundle(fxglServicesBundle);
    }
}
