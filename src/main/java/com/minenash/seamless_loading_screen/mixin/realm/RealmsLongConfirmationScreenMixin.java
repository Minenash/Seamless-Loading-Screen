package com.minenash.seamless_loading_screen.mixin.realm;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minenash.seamless_loading_screen.ScreenshotLoader;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.realms.gui.screen.RealmsLongConfirmationScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Mixin(RealmsLongConfirmationScreen.class)
public class RealmsLongConfirmationScreenMixin extends Screen {

    protected RealmsLongConfirmationScreenMixin(Text title) { super(title); }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/realms/gui/screen/RealmsLongConfirmationScreen;renderBackground(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    public void render(RealmsLongConfirmationScreen screen, MatrixStack stack) {
        this.renderBackground(stack);
        ScreenshotLoader.render(screen, stack);
    }

}
