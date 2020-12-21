package com.minenash.seamless_loading_screen;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

import java.io.File;

public class SeamlessLoadingScreen implements ClientModInitializer {

    public static boolean changeWorldJoinScreen = false;

    @Override
    public void onInitializeClient() {
        File file = new File(MinecraftClient.getInstance().runDirectory, "screenshots/worlds");
        file.mkdir();
        File file2 = new File(file, "singleplayer");
        file2.mkdir();
        file2 = new File(file, "servers");
        file2.mkdir();
    }
}
