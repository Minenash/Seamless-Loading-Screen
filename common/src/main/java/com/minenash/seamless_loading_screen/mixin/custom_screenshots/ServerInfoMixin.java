package com.minenash.seamless_loading_screen.mixin.custom_screenshots;

import com.minenash.seamless_loading_screen.DisplayMode;
import com.minenash.seamless_loading_screen.ServerInfoExtension;
import com.minenash.seamless_loading_screen.config.Config;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerInfo.class)
public class ServerInfoMixin implements ServerInfoExtension {

    @Shadow
    public String address;
    @Unique
    private DisplayMode seamless_loading_screen$displayMode = DisplayMode.ENABLED;

    @Inject(method = "fromNbt", at = @At("RETURN"))
    private static void deserialize(NbtCompound tag, CallbackInfoReturnable<ServerInfo> callback) {
//        System.out.println("READ: ");
        if (!tag.contains("screenshotDisplayMode", 8)) return;

        ((ServerInfoMixin) (Object) callback.getReturnValue()).seamless_loading_screen$displayMode = Enum.valueOf(DisplayMode.class, tag.getString("screenshotDisplayMode"));
    }

    @Inject(method = "toNbt", at = @At("RETURN"))
    private void serialize(CallbackInfoReturnable<NbtCompound> callback) {
//        System.out.println("SET: " + displayMode.toString());
        callback.getReturnValue().putString("screenshotDisplayMode", seamless_loading_screen$displayMode.toString());
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copyFrom(ServerInfo info, CallbackInfo callback) {
        seamless_loading_screen$displayMode = ((ServerInfoMixin) (Object) info).seamless_loading_screen$displayMode;
    }

    @Override
    public DisplayMode getDisplayMode() {
        for (String blacklistedAddress : Config.get().blacklistedAddresses) {
            if (this.address.contains(blacklistedAddress)) {
                return DisplayMode.DISABLED;
            }
        }

        return seamless_loading_screen$displayMode;
    }

    @Override
    public void setDisplayMode(DisplayMode mode) {
        seamless_loading_screen$displayMode = mode;
    }
}
