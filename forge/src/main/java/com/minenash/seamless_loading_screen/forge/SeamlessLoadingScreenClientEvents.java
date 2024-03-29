package com.minenash.seamless_loading_screen.neoforge;

import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import com.minenash.seamless_loading_screen.config.SeamlessLoadingScreenConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SeamlessLoadingScreen.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SeamlessLoadingScreenClientEvents {

    @SubscribeEvent
    public static void onPostInit(FMLClientSetupEvent event) {
        ModList.get().getModContainerById(SeamlessLoadingScreen.MODID).ifPresent(modContainer -> {
            modContainer.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                    new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> SeamlessLoadingScreenConfig.getInstance().generateScreen(parent)));
        });
    }
}

