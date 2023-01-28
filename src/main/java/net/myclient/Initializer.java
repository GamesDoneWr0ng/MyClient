package net.myclient;

import net.fabricmc.api.ModInitializer;

public final class Initializer implements ModInitializer {
    private static boolean initialized;

    @Override
    public void onInitialize()
    {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        if(initialized)
            throw new RuntimeException(
                    "WurstInitializer.onInitialize() ran twice!");

        Main.INSTANCE.initialize();
        initialized = true;
    }
}