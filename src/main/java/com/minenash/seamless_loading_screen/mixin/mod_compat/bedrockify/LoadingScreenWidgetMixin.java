package com.minenash.seamless_loading_screen.mixin.mod_compat.bedrockify;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget", remap = false)
public class LoadingScreenWidgetMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void renderBackground(MatrixStack matrices, int _width, int _height, Text _title, Text _message, int _progress, CallbackInfo _info) {
        if (ScreenshotLoader.loaded)
            ScreenshotLoader.render(MinecraftClient.getInstance().currentScreen, matrices);
    }

}
