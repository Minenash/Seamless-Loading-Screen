package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.Config;
import com.minenash.seamless_loading_screen.config.MidnightConfig;
import com.mojang.logging.LogUtils;
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

    public static void onInitializeClient() {
        MidnightConfig.init(MODID, Config.class);
    }

    public static void openSettingsScreen(MinecraftClient client){
        client.setScreen(Config.getScreen(client.currentScreen, MODID));
    }
}
