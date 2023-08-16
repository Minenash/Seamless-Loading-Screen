package com.minenash.seamless_loading_screen.config;

import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuEntryPoint implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> MidnightConfig.getScreen(parent, SeamlessLoadingScreen.MODID);
    }

}