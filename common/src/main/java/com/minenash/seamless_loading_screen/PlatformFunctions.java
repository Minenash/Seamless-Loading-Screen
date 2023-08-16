package com.minenash.seamless_loading_screen;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PlatformFunctions {

    /**
     * This is our actual method to {@link PlatformFunctions#getConfigDirectory()}.
     */
    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isClientEnv() {
        throw new AssertionError();
    }

    //---

    @ExpectPlatform
    public static Path getGameDir() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isDevEnv(){
        throw new AssertionError();
    }
}
