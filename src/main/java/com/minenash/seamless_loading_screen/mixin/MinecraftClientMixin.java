package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

	@Shadow protected abstract void reset(Screen screen);

	@Shadow private static MinecraftClient instance;

	@Shadow public abstract void scheduleStop();

	@Inject(method = "joinWorld", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
	private void changeScreen(ClientWorld world, CallbackInfo ci) {
		if (SeamlessLoadingScreen.changeWorldJoinScreen) {
			SeamlessLoadingScreen.changeWorldJoinScreen = false;
			ScreenshotLoader.inFade = true;
		}
	}

	@Unique private boolean firstOccurrence = true;

	@Inject(method = "scheduleStop", at = @At("HEAD"), cancellable = true)
	private void onWindowClose(CallbackInfo info) {
		if (!firstOccurrence || instance.player == null) return;

		OnQuitHelper.beginScreenshotTask(() -> {
			this.firstOccurrence = false;

			this.scheduleStop();
		});

		info.cancel();
	}

}
