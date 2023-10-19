package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.OnLeaveHelper;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public abstract class ClientCommonNetworkHandlerMixin {
    @Shadow public abstract void onDisconnected(Text reason);

    @Unique
    private boolean seamless_loading_screen$haltDisconnect = true;

    @Inject(method = "onDisconnected", at = @At("HEAD"), cancellable = true)
    private void onServerOrderedDisconnect(Text reason, CallbackInfo info) {
        if (seamless_loading_screen$haltDisconnect) {
            OnLeaveHelper.beginScreenshotTask(() -> this.onDisconnected(reason));

            info.cancel();
        }

        seamless_loading_screen$haltDisconnect = !seamless_loading_screen$haltDisconnect;
    }
}
