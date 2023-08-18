package com.minenash.seamless_loading_screen.mixin.custom_screenshots;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import com.minenash.seamless_loading_screen.ServerInfoExtension;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {

    @Inject(method = "connect(Lnet/minecraft/client/network/ServerInfo;)V", at = @At("HEAD"))
    private void getAllowCustomScreenshot(ServerInfo info, CallbackInfo callback) {
        ScreenshotLoader.displayMode = ((ServerInfoExtension)info).getDisplayMode();
    }

}
