package com.minenash.seamless_loading_screen.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PlatformFunctions {

    /**
     * This is our actual method to {@link PlatformFunctions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
    public static boolean isClientEnv() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
