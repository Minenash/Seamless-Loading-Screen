package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.SeamlessLoadingScreenConfig;
import com.minenash.seamless_loading_screen.compat.distant_horizons.SeamlessLoadingScreenDhRegistry;
import com.mojang.logging.LogUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SeamlessLoadingScreen {

    public static final String MODID = "seamless_loading_screen";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static boolean changeWorldJoinScreen = false;

    public static ScreenshotLoader.BlurHelper BLUR_PROGRAM = new ScreenshotLoader.BlurHelper();

    public static final String DISTANT_HORIZONS_MOD_ID = "distanthorizons";

    public static void onInitializeClient() {
        SeamlessLoadingScreenConfig.load();

        try {
            Path path = PlatformFunctions.getGameDir().resolve("screenshots/worlds");
            Files.createDirectories(path.resolve("singleplayer"));
            Files.createDirectories(path.resolve("servers"));
            Files.createDirectories(path.resolve("realms"));
            Files.createDirectories(path.resolve("archive"));
        } catch (IOException e) {
            LOGGER.error("[SeamlessLoadingScreen] A problem when creating the various needed Directories, there might be problems!", e);
        }

        if (FabricLoader.getInstance().isModLoaded(DISTANT_HORIZONS_MOD_ID)) {
            SeamlessLoadingScreenDhRegistry.register();
        }
    }

    public static void openSettingsScreen(MinecraftClient client) {
        client.setScreen(SeamlessLoadingScreenConfig.getInstance().generateScreen(client.currentScreen));
    }
}
