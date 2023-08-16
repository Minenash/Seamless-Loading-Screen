package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelStorage.class)
public class LevelStorageMixin {

    //TODO: Issue within bobby due to stuff!
    @Inject(method = "levelExists", at = @At("HEAD"))
    private void getLevelName(String level, CallbackInfoReturnable<Boolean> _info) {
        ScreenshotLoader.setScreenshot(level);
    }

}
