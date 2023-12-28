package com.minenash.seamless_loading_screen.neoforge;

import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;


@Mod(SeamlessLoadingScreen.MODID)
public class SeamlessLoadingScreenForge {

    public SeamlessLoadingScreenForge(IEventBus modEventBus, Dist dist) {
        //Is this needed?
        ModLoadingContext.get()
                .registerExtensionPoint(
                        IExtensionPoint.DisplayTest.class,
                        () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (remote, server) -> true)
                );

        if(dist.isClient()){
            SeamlessLoadingScreen.onInitializeClient();
        }
    }
}
