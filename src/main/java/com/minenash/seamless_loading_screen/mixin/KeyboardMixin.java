package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import com.minenash.seamless_loading_screen.config.ConfigScreen;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"))
    private void checkKeyBind(long window, int key, int scancode, int i, int j, CallbackInfo info) {
        if(window == client.getWindow().getHandle() && SeamlessLoadingScreen.OPEN_SETTINGS.matchesKey(key, scancode)) {
            client.openScreen(new ConfigScreen(null));
        }
    }

}
