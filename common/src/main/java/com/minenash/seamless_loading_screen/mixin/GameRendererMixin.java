package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.OnLeaveHelper;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V", shift = At.Shift.BY, by = 2)) //
    private void attemptToTakeScreenshot(float tickDelta, long startTime, boolean tick, CallbackInfo ci){
        if(OnLeaveHelper.attemptScreenShot) OnLeaveHelper.takeScreenShot();
    }

}
