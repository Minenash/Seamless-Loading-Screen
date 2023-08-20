package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.QuickPlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(QuickPlay.class)
public class QuickPlayMixin {

    @Inject(method = "startSingleplayer", at = @At("HEAD"))
    private static void getLevelName(MinecraftClient client, String levelName, CallbackInfo ci) {
        ScreenshotLoader.setScreenshot(levelName);
    }

}
