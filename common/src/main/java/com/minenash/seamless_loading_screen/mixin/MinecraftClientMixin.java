package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.FadeScreen;
import com.minenash.seamless_loading_screen.OnLeaveHelper;
import com.minenash.seamless_loading_screen.ScreenshotLoader;
import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import com.minenash.seamless_loading_screen.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftClient.class, priority = 900)
public abstract class MinecraftClientMixin {

    @Shadow
    static MinecraftClient instance;
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    @Nullable
    public Screen currentScreen;

    @Shadow
    @Nullable
    public ClientWorld world;
    @Unique
    private boolean seamless_loading_screen$terrainScreenReplaced = false;
    @Unique
    private boolean seamless_loading_screen$firstOccurrence = true;
    @Shadow
    @Final
    private Window window;

    @Shadow
    public abstract void scheduleStop();

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Inject(method = "joinWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void changeScreen(ClientWorld world, CallbackInfo ci) {
        if (SeamlessLoadingScreen.changeWorldJoinScreen) {
            SeamlessLoadingScreen.changeWorldJoinScreen = false;
            ScreenshotLoader.inFade = true;

            this.seamless_loading_screen$terrainScreenReplaced = false;
        }
    }

    @ModifyVariable(method = "setScreen", at = @At(value = "HEAD"), argsOnly = true, index = 1)
    private Screen fadeScreen(Screen screen) {
        if (currentScreen instanceof DownloadingTerrainScreen && world != null && ScreenshotLoader.loaded && ScreenshotLoader.inFade) {
            if (screen == null) {
                this.seamless_loading_screen$terrainScreenReplaced = true;

                return new FadeScreen(Config.get().time, Config.get().fade).then((forced) -> {
                    if (!forced) setScreen(null);
                    ScreenshotLoader.inFade = false;
                });
            } else {
                LOGGER.warn("[SeamlessLoadingScreen] Fade screen has been skipped due to someone replacing the screen before we could add our own after DownloadingTerrainScreen");

                ScreenshotLoader.inFade = false;
            }

            ScreenshotLoader.replacebg = false;
        }
        return screen;
    }

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void failSafe(Screen screen, CallbackInfo ci) {
        //Failsafe injection to prevent mouse lockup or background issues due to other mod injection stuff
        if ((seamless_loading_screen$terrainScreenReplaced && !(screen instanceof FadeScreen)) || (screen == null && (ScreenshotLoader.inFade || ScreenshotLoader.replacebg))) {
            ScreenshotLoader.inFade = false;
            ScreenshotLoader.replacebg = false;

            this.seamless_loading_screen$terrainScreenReplaced = false;
        }
    }

    //----

    @Inject(method = "scheduleStop", at = @At("HEAD"), cancellable = true)
    private void onWindowClose(CallbackInfo info) {
        if (!seamless_loading_screen$firstOccurrence || instance.player == null) return;

        OnLeaveHelper.beginScreenshotTask(() -> {
            this.seamless_loading_screen$firstOccurrence = false;

            this.scheduleStop();
        });

        info.cancel();
    }

    @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    private void captureResize(CallbackInfo ci) {
        if (SeamlessLoadingScreen.BLUR_PROGRAM.loaded) {
            SeamlessLoadingScreen.BLUR_PROGRAM.onWindowResize((MinecraftClient) (Object) this, window);
        }
    }

}
