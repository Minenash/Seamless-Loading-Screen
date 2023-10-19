package com.minenash.seamless_loading_screen.fabric.config;

import com.minenash.seamless_loading_screen.config.SeamlessLoadingScreenConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuEntryPoint implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> SeamlessLoadingScreenConfig.getInstance().generateScreen(parent);
    }

}