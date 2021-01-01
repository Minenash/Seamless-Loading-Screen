package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelStorage.class)
public class LevelStorageMixin {

    @Inject(method = "levelExists", at = @At("HEAD"))
    private void getLevelName(String level, CallbackInfo _info) {
        ScreenshotLoader.setScreenshot(level);
    }

}
