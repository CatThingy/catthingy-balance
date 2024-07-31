package dev.catthingy;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatThingyBalance implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("catthingy-balance");

    @Override
    public void onInitialize() {
        CustomDamageTypes.init();
        CustomEnchantmentEffects.init();
    }
}
