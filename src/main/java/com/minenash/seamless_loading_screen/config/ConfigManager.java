package com.minenash.seamless_loading_screen.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class ConfigManager {

    private static final File configFile = FabricLoader.getInstance().getConfigDir().resolve("seamless_loading_screen.json").toFile();
    private static final Logger LOGGER = LogManager.getLogger("ServerModList");

    private static final String ERR_COLOR = "\u001B[31m";
    private static final String MOD_NAME = "\033[0;33mSeamless Loading Screen\033[0m";

    public static int time = 80;
    public static int fade = 20;
    public static boolean disableCamera = true;

    public static void load() {
        try {
            if (!configFile.exists())
                saveConfig();
            if (configFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                JsonObject config = new Gson().fromJson(reader, JsonObject.class);
                JsonElement time = config.get("time");
                JsonElement fade = config.get("fade");
                JsonElement disableCamera = config.get("disable_camera");

                if (time != null) ConfigManager.time = time.getAsInt();
                if (fade != null) ConfigManager.fade = fade.getAsInt();
                if (disableCamera != null && !disableCamera.getAsBoolean()) ConfigManager.disableCamera = false;

            }
        }
        catch (FileNotFoundException e) {
            LOGGER.error(MOD_NAME + ERR_COLOR + ": Couldn't load config; reverting to defaults");
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        LOGGER.info(MOD_NAME + ": Saving config.");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject config = new JsonObject();
        config.addProperty("time", time);
        config.addProperty("fade", fade);
        config.addProperty("disable_camera", disableCamera);

        try (FileWriter fileWriter = new FileWriter(configFile)) {
            fileWriter.write(gson.toJson(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
