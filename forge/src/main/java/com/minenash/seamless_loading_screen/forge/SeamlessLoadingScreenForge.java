package com.minenash.seamless_loading_screen.neoforge;

import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SeamlessLoadingScreen.MODID)
public class SeamlessLoadingScreenForge {

    public SeamlessLoadingScreenForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //Is this needed?
        ModLoadingContext.get()
                .registerExtensionPoint(
                        IExtensionPoint.DisplayTest.class,
                        () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (remote, server) -> true)
                );

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SeamlessLoadingScreen::onInitializeClient);
        //DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> SeamlessLoadingScreenShaderInit.init(eventBus));
    }
}
