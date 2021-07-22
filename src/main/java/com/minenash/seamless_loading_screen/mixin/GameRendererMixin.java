package com.minenash.seamless_loading_screen.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import com.minenash.seamless_loading_screen.config.Config;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private MinecraftClient client;
    private MatrixStack stack;

    @Redirect(method = "render", at = @At(value = "NEW", target = "net/minecraft/client/util/math/MatrixStack"))
    private MatrixStack getStack() {
        stack = new MatrixStack();
        return stack;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"), cancellable = true)
    private void fade(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
        if (ScreenshotLoader.loaded && ScreenshotLoader.time > 0) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ScreenshotLoader.SCREENSHOT);

            int height = client.getWindow().getScaledHeight();
            int width = client.getWindow().getScaledWidth();
            int w = (int) (ScreenshotLoader.imageRatio * height);
            boolean doFade = ScreenshotLoader.time <= Config.fade;

            if (doFade)
                RenderSystem.clearColor(1.0F, 1.0F, 1.0F, ScreenshotLoader.timeDelta * ScreenshotLoader.time);
            DrawableHelper.drawTexture(stack, width/2 - w/2, 0, 0.0F, 0.0F, w, height, w, height);
            //ScreenshotLoader.renderVignette();

            if (!doFade)
                DrawableHelper.drawCenteredText(stack, client.textRenderer, new TranslatableText("seamless_loading_screen.screen.generating_chunks"), width / 2, 70, 16777215);

            ScreenshotLoader.time--;
        }
        else if (ScreenshotLoader.loaded && ScreenshotLoader.time == 0) {
            ScreenshotLoader.loaded = false;
            ScreenshotLoader.inFade = false;
            ScreenshotLoader.time = Config.time;
        }
    }

}
