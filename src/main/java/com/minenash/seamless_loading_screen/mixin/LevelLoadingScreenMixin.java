package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {

    protected LevelLoadingScreenMixin(Text title) { super(title); }

    @ModifyArg(method = "drawChunkMap",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"),
            index = 4,
            slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", ordinal = 3))
    )
    private static int changeBackgroundColor(int color){
        return color == -16777216 ? 0xAA000000 : color;
    }

//    @Redirect(method = "drawChunkMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/LevelLoadingScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
//    private static void makeBackgroundTranslucent(MatrixStack stack, int x1, int y1, int x2, int y2, int color) {
//            fill(stack, x1, y1, x2, y2, color == -16777216 ? 0xAA000000 : color);
//    }

}
