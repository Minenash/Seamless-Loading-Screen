package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.*;
import com.minenash.seamless_loading_screen.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

	@Shadow static MinecraftClient instance;

	@Shadow public abstract void scheduleStop();

	@Shadow @Nullable public Screen currentScreen;

	@Shadow @Nullable public ClientWorld world;

	@Shadow public abstract void setScreen(@Nullable Screen screen);

	@Inject(method = "joinWorld", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
	private void changeScreen(ClientWorld world, CallbackInfo ci) {
		if (SeamlessLoadingScreen.changeWorldJoinScreen) {
			SeamlessLoadingScreen.changeWorldJoinScreen = false;
			ScreenshotLoader.inFade = true;
		}
	}

	@Unique private boolean firstOccurrence = true;

	@ModifyVariable(method = "setScreen", at = @At(value = "HEAD"), argsOnly = true, index = 1)
	private Screen fadeScreen(Screen screen) {
		if(currentScreen instanceof DownloadingTerrainScreen && screen == null && world != null && ScreenshotLoader.loaded) {
			return new FadeScreen(Config.get().time, Config.get().fade).then((forced) -> {
				if(!forced) setScreen(null);
				ScreenshotLoader.inFade = false;
			});
		}
		return screen;
	}

	@Inject(method = "scheduleStop", at = @At("HEAD"), cancellable = true)
	private void onWindowClose(CallbackInfo info) {
		if (!firstOccurrence || instance.player == null) return;

		OnLeaveHelper.beginScreenshotTask(() -> {
			this.firstOccurrence = false;

			this.scheduleStop();
		});

		info.cancel();
	}

	//----

	@Shadow
	@Final
	private Window window;

	@Inject(method = "onResolutionChanged", at = @At("TAIL"))
	private void captureResize(CallbackInfo ci) {
		if(SeamlessLoadingScreen.BLUR_PROGRAM.loaded) {
			SeamlessLoadingScreen.BLUR_PROGRAM.onWindowResize((MinecraftClient) (Object) this, window);
		}
	}

}
