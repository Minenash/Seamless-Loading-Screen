package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {

    @Unique
    private static boolean alreadyRenderedBackgroundImage = false;

    @Inject(method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("TAIL"))
    private void renderScreenBackground_AfterRenderCall(MatrixStack matrices, CallbackInfo ci){
        if(ScreenshotLoader.stopReplacement) return;

        if(!alreadyRenderedBackgroundImage && ScreenshotLoader.validScreen((Screen) (Object) this)){
            ScreenshotLoader.render((Screen) (Object) this, matrices);
        }

        alreadyRenderedBackgroundImage = false;
    }

    @Inject(method = "renderBackgroundTexture", at = @At("TAIL"))
    private void renderScreenBackground_AfterTexture(int vOffset, CallbackInfo ci){
        if(ScreenshotLoader.stopReplacement) return;

        if(ScreenshotLoader.validScreen((Screen) (Object) this)){
            ScreenshotLoader.render((Screen) (Object) this, new MatrixStack());
            alreadyRenderedBackgroundImage = true;
        }
    }
}
