package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SeamlessLoadingScreen implements ClientModInitializer {
    public static final String MODID = "seamless_loading_screen";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static boolean changeWorldJoinScreen = false;
    public static boolean isDisconnecting = false; //Fapi 0.30.0 Compat

    public static final KeyBinding OPEN_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "seamless_loading_screen.keybind.config",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G,
            "key.categories.misc"));

    @Override
    public void onInitializeClient() {
        Config.init(MODID, Config.class);

        try {
            Path path = FabricLoader.getInstance().getGameDir().resolve("screenshots/worlds");
            Files.createDirectories(path.resolve("singleplayer"));
            Files.createDirectories(path.resolve("servers"));
            Files.createDirectories(path.resolve("realms"));
            Files.createDirectories(path.resolve("archive"));
        } catch (IOException e) {
            LOGGER.error("Failed to create screenshot directories.", e);
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_SETTINGS.wasPressed())
                client.setScreen(Config.getScreen(client.currentScreen));
        });
    }
}
