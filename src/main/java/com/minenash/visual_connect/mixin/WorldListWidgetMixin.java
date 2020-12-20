package com.minenash.visual_connect.mixin;

import com.minenash.visual_connect.ScreenshotLoader;
import com.minenash.visual_connect.ScreenshotWithTextScreen;
import net.minecraft.client.MinecraftClient;
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

    @Shadow @Final private LevelSummary level;

    @Inject(method = "play", at = @At("HEAD"))
    public void setFilename(CallbackInfo info) {
        ScreenshotLoader.setScreenshot(level.getName());
    }

    @Redirect(method = "start", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/WorldListWidget$Entry;method_29990()V"))
    private void changeScreen(WorldListWidget.Entry entry) {
        MinecraftClient.getInstance().method_29970(new ScreenshotWithTextScreen(new TranslatableText("selectWorld.data_read")));
    }
}
