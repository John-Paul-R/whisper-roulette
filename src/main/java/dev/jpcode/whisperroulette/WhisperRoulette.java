package dev.jpcode.whisperroulette;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class WhisperRoulette implements ModInitializer
{
    public static final Logger LOGGER = LogManager.getLogger("whisperroulette");

    @Override
    public void onInitialize()
    {
        LOGGER.info("Whisper Roulette is getting ready...");

        WRCommandRegistry.register();

        LOGGER.info("Whisper Roulette is ready!");
    }
}
