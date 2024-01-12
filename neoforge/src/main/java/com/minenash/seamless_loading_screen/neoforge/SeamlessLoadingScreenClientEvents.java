package com.minenash.seamless_loading_screen.neoforge;

import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import com.minenash.seamless_loading_screen.config.SeamlessLoadingScreenConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;

@Mod.EventBusSubscriber(modid = SeamlessLoadingScreen.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SeamlessLoadingScreenClientEvents {

    @SubscribeEvent
    public static void onPostInit(FMLClientSetupEvent event) {
        ModList.get().getModContainerById(SeamlessLoadingScreen.MODID).ifPresent(modContainer -> {
            modContainer.registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> SeamlessLoadingScreenConfig.getInstance().generateScreen(parent))
            );
        });
    }
}

