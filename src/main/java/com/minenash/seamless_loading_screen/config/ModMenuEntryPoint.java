package com.minenash.seamless_loading_screen.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;

public class ModMenuEntryPoint implements ModMenuApi {

    @Override
    public String getModId() {
        return "seamless_loading_screen";
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return Config::getScreen;
    }
}