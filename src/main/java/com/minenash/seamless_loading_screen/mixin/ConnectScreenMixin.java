package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ConnectScreen;renderBackground(Lnet/minecraft/client/util/math/MatrixStack;)V"))
	public void render(ConnectScreen screen, MatrixStack stack) {
		screen.renderBackground(stack);
		ScreenshotLoader.render(screen,stack);
	}

	@Inject(method = "Lnet/minecraft/client/gui/screen/ConnectScreen;connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V", at = @At("HEAD"))
	public void getImage(MinecraftClient client, ServerAddress address, ServerInfo info, CallbackInfo ci) {
		ScreenshotLoader.setScreenshot(address.getAddress(), address.getPort());
	}
}
