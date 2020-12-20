package com.minenash.visual_connect.mixin;

import com.minenash.visual_connect.FinishQuit;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin {

    @Inject(method = "method_19836", at = @At(value = "HEAD"), cancellable = true)
    public void takeScreenshotBeforeDisconnect(ButtonWidget button, CallbackInfo info) {
        FinishQuit.run();
        info.cancel();
    }

}
