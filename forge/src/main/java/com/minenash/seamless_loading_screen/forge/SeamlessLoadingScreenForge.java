package com.minenash.seamless_loading_screen.forge;

import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;

@Mod(SeamlessLoadingScreen.MODID)
public class SeamlessLoadingScreenForge {
    public SeamlessLoadingScreenForge() {
        //Is this needed?
        ModLoadingContext.get()
                .registerExtensionPoint(
                        IExtensionPoint.DisplayTest.class,
                        () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (remote, server) -> true)
                );

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SeamlessLoadingScreen::onInitializeClient);
    }
}
