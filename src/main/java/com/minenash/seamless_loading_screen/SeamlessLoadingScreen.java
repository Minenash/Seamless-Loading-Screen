package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.Config;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.impl.screenhandler.client.ClientNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SeamlessLoadingScreen implements ClientModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static boolean changeWorldJoinScreen = false;
    public static boolean isDisconnecting = false; //Fapi 0.30.0 Compat

    public static final KeyBinding OPEN_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "seamless_loading_screen.keybind.config",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G,
            "key.categories.misc"));

    @Override
    public void onInitializeClient() {
        Config.init("seamless_loading_screen", Config.class);

        try {
            Path path = FabricLoader.getInstance().getGameDir().resolve("screenshots/worlds");
            Files.createDirectories(path.resolve("singleplayer"));
            Files.createDirectories(path.resolve("servers"));
            Files.createDirectories(path.resolve("realms"));
            Files.createDirectories(path.resolve("archive"));
        } catch (IOException e) {
            LOGGER.error("[SeamlessLoadingScreen] A problem when creating the various needed Directories, there might be problems!", e);
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_SETTINGS.wasPressed()) client.setScreen(Config.getScreen(client.currentScreen));
        });

    }
}
