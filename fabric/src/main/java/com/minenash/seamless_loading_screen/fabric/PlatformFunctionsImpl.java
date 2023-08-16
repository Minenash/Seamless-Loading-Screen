package com.minenash.seamless_loading_screen.fabric;

import com.minenash.seamless_loading_screen.PlatformFunctions;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PlatformFunctionsImpl {

    /**
     * This is our actual method to {@link PlatformFunctions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static boolean isClientEnv() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    //--

    public static Path getGameDir() {
        return FabricLoader.getInstance().getGameDir();
    }

    public static boolean isDevEnv(){
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
