package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import com.minenash.seamless_loading_screen.ScreenshotWithTextScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldListWidget.Entry.class)
public class WorldListWidgetMixin {

    @Shadow @Final LevelSummary level;

    @Inject(method = "play", at = @At("HEAD"))
    public void setFilename(CallbackInfo info) {
        ScreenshotLoader.setScreenshot(level.getName());
    }

    @Redirect(method = "openReadingWorldScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreenAndRender(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void changeScreen(MinecraftClient client, Screen screen) {
        client.setScreenAndRender(new ScreenshotWithTextScreen(new TranslatableText("selectWorld.data_read")));
    }
}
