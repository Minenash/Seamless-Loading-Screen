package com.minenash.seamless_loading_screen.mixin.mod_compat.fapi_0_30_0;

import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.impl.client.screen.ScreenExtensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.fabricmc.fabric.api.client.screen.v1.ScreenEvents", remap = false)
public class ScreenEventsMixin {

    @Inject(method = "afterRender", at = @At("HEAD"), cancellable = true)
    private static void ignoreNullCheckIfDisconnecting(Screen screen, CallbackInfoReturnable<Event<ScreenEvents.AfterRender>> info) {
        if (SeamlessLoadingScreen.isDisconnecting && screen == null) {
            SeamlessLoadingScreen.isDisconnecting = false;
            info.setReturnValue(ScreenExtensions.getExtensions(MinecraftClient.getInstance().currentScreen).fabric_getAfterRenderEvent());
            info.cancel();
        }
    }

}
