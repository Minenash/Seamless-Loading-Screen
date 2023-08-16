package com.minenash.seamless_loading_screen.mixin.custom_screenshots;

import com.minenash.seamless_loading_screen.ServerInfoExtras;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerInfo.class)
public class ServerInfoMixin implements ServerInfoExtras {

    @Unique
    private boolean allowCustomScreenshots = false;

    @Inject(method = "toNbt", at = @At("RETURN"))
    private void serialize(CallbackInfoReturnable<NbtCompound> callback) {
        callback.getReturnValue()
                .putBoolean("allowCustomScreenshots", allowCustomScreenshots);
    }

    @Inject(method = "fromNbt", at = @At("RETURN"))
    private static void deserialize(NbtCompound tag, CallbackInfoReturnable<ServerInfo> callback) {
        if (!tag.contains("allowCustomScreenshots", 1)) return;

        ((ServerInfoMixin) (Object) callback.getReturnValue()).allowCustomScreenshots = tag.getBoolean("allowCustomScreenshots");
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copyFrom(ServerInfo info, CallbackInfo callback) {
        allowCustomScreenshots = ((ServerInfoMixin)(Object)info).allowCustomScreenshots;
    }

    @Override
    public void setAllowCustomScreenshots(boolean b) {
        allowCustomScreenshots = b;
    }

    @Override
    public boolean getAllowCustomScreenshot() {
        return allowCustomScreenshots;
    }
}
