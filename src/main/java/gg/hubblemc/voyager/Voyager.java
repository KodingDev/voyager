package gg.hubblemc.voyager;

import gg.hubblemc.voyager.fixes.LogPatcher;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Voyager implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(Voyager.class);

    @Override
    public void onInitialize() {
        LOGGER.info("Time to blast off, Voyager!");

        try {
            LogPatcher.reconfigureLog4j();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("Failed to patch log4j", e);
        }
    }
}
