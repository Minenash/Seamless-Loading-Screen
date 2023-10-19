package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import com.minenash.seamless_loading_screen.config.SeamlessLoadingScreenConfig;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onCursorPos", at = @At("HEAD"), cancellable = true)
    private void pauseMouseMovement(CallbackInfo info) {
        if (SeamlessLoadingScreenConfig.get().disableCamera && ScreenshotLoader.inFade) info.cancel();
    }

}
