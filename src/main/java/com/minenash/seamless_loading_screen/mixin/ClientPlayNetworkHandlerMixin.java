package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.FinishQuit;
import com.minenash.seamless_loading_screen.ScreenshotLoader;
import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.realms.gui.screen.DisconnectedRealmsScreen;
import net.minecraft.client.realms.gui.screen.RealmsScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow @Final private Screen loginScreen;

    @Shadow @Final private static Text field_26620;

    @Redirect(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;joinWorld(Lnet/minecraft/client/world/ClientWorld;)V"))
    private void setChangeWorldJoinScreen(MinecraftClient client, ClientWorld world) {
        if (ScreenshotLoader.loaded) {
            SeamlessLoadingScreen.changeWorldJoinScreen = true;
        }
        client.joinWorld(world);
    }

    private boolean stopDisconnect = true;
    @Inject(method = "onDisconnected", at = @At("HEAD"), cancellable = true)
    private void onServerOrderedDisconnect(Text reason, CallbackInfo info) {
        if (stopDisconnect) {
            info.cancel();
            FinishQuit.run((ClientPlayNetworkHandler) (Object) this, reason);
        }
        stopDisconnect = !stopDisconnect;
    }
}
