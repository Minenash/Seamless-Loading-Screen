package com.minenash.seamless_loading_screen.mixin.realm;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.realms.gui.screen.RealmsLongRunningMcoTaskScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RealmsLongRunningMcoTaskScreen.class)
public class RealmsLongRunningMcoTaskScreenMixin extends Screen {

    protected RealmsLongRunningMcoTaskScreenMixin(Text title) { super(title); }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/realms/gui/screen/RealmsLongRunningMcoTaskScreen;renderBackground(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    public void render(RealmsLongRunningMcoTaskScreen screen, MatrixStack stack) {
        this.renderBackground(stack);
        ScreenshotLoader.render(screen, stack);
    }

}
