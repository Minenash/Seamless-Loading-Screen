package com.minenash.seamless_loading_screen.mixin;

import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {

    protected LevelLoadingScreenMixin(Text title) { super(title); }

    @ModifyArg(method = "drawChunkMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"), index = 5)
    private static int changeBackgroundColor(int color){
        return color == 0xFF000000 ? 0xAA000000 : color;
    }
}
