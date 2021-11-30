package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.*;
import com.minenash.seamless_loading_screen.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

	@Shadow protected abstract void reset(Screen screen);

	@Shadow private static MinecraftClient instance;

	@Shadow public abstract void setScreen(@Nullable Screen screen);

	@Shadow @Nullable public Screen currentScreen;

	@Shadow @Nullable public ClientWorld world;

	@Shadow @Nullable public ClientPlayerEntity player;

	@Redirect(method = "joinWorld", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
	private void changeScreen(MinecraftClient _client, Screen screen) {
		if (SeamlessLoadingScreen.changeWorldJoinScreen) {
			reset(new ScreenshotWithTextScreen(new TranslatableText("connect.joining")));
			SeamlessLoadingScreen.changeWorldJoinScreen = false;
			ScreenshotLoader.inFade = true;
		}
		else
			reset(screen);
	}

	@ModifyVariable(method = "setScreen", at = @At(value = "HEAD"), argsOnly = true, index = 1)
	private Screen fadeScreen(Screen screen) {
		if(currentScreen instanceof DownloadingTerrainScreen && screen == null && world != null) {
			return new FadeScreen(Config.time, Config.fade).then((forced) -> {
				if(!forced) setScreen(null);
				ScreenshotLoader.inFade = false;
			});
		}
		return screen;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;disconnect()V"), method = "startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V")
	private void changeScreen(MinecraftClient client) {
		client.disconnect(new ScreenshotWithTextScreen());
	}

	@Redirect(method = "method_29970", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
	private void changeScreen2(MinecraftClient client, Screen screen) {
		client.setScreen(new ScreenshotWithTextScreen(screen.getTitle()));
	}

	boolean first = true;
	@Inject(method = "scheduleStop", at = @At("HEAD"), cancellable = true)
	private void onWindowClose(CallbackInfo info) {
		if (!first || instance.player == null) return;

		FinishQuit.run(true);
		first = false;
		info.cancel();
	}

//	@Inject(method = "openScreen", at = @At("HEAD"))
//	public void openScreen(Screen screen, CallbackInfo _info) {
//		if (screen != null)
//			System.out.println("Screen: " + screen.getTitle().asString() + ", " + screen);
//		else
//			System.out.println("Screen: null");
//	}

}
