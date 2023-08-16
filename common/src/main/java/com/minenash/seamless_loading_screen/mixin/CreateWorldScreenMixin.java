package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

    @Shadow private String saveDirectoryName;

    @Inject(method = "createLevel", at = @At("HEAD"))
    private void setFilename(CallbackInfo ci){
        ScreenshotLoader.setScreenshot(saveDirectoryName);
    }
}
