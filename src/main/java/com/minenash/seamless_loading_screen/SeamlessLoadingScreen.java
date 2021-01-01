package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SeamlessLoadingScreen implements ClientModInitializer {

    public static boolean changeWorldJoinScreen = false;

    public static final KeyBinding OPEN_SETTINGS = new KeyBinding("Open Seamless Loading Screen Config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.misc");

    @Override
    public void onInitializeClient() {
        ConfigManager.load();

        try {
            Path path = FabricLoader.getInstance().getGameDir().resolve("screenshots/worlds");
            Files.createDirectories(path.resolve("singleplayer"));
            Files.createDirectory(path.resolve("servers"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
