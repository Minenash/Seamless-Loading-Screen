package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.OnLeaveHelper;
import com.minenash.seamless_loading_screen.ScreenshotLoader;
import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow public abstract void onDisconnected(Text reason);

    @Inject(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;joinWorld(Lnet/minecraft/client/world/ClientWorld;)V"))
    private void setChangeWorldJoinScreen(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (ScreenshotLoader.loaded) SeamlessLoadingScreen.changeWorldJoinScreen = true;
    }

    @Unique private boolean haltDisconnect = true;

    @Inject(method = "onDisconnected", at = @At("HEAD"), cancellable = true)
    private void onServerOrderedDisconnect(Text reason, CallbackInfo info) {
        if (haltDisconnect) {
            OnLeaveHelper.beginScreenshotTask(() -> this.onDisconnected(reason));

            info.cancel();
        }

        haltDisconnect = !haltDisconnect;
    }

}
