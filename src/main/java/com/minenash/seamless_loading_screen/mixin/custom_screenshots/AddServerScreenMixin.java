package com.minenash.seamless_loading_screen.mixin.custom_screenshots;

import com.minenash.seamless_loading_screen.ServerInfoExtras;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AddServerScreen.class)
public abstract class AddServerScreenMixin extends Screen {

    @Shadow @Final private ServerInfo server;

    private ButtonWidget buttonAllowCustomScreenshot;

    protected AddServerScreenMixin(Text title) { super(title); }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;", ordinal = 0))
    private ButtonWidget.Builder buttonAdd(ButtonWidget.Builder instance, int x, int y, int width, int height) {
        return instance.dimensions(x+103,y+24,width-103,height);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;", ordinal = 1))
    private ButtonWidget.Builder buttonCancel(ButtonWidget.Builder instance, int x, int y, int width, int height) {
        return instance.dimensions(x,y,width-103,height);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void buttonAllowCustomScreenshot(CallbackInfo info) {
        buttonAllowCustomScreenshot = addDrawableChild(ButtonWidget.builder(getText(),
                        buttonWidget -> {
                            ((ServerInfoExtras)server).setAllowCustomScreenshots(!((ServerInfoExtras)server).getAllowCustomScreenshot());
                            buttonAllowCustomScreenshot.setMessage(getText());
                        })
                .dimensions(width / 2 - 100, height / 4 + 72 + 24, 200, 20)
                .build());
    }

    private Text getText() {
        return (Text.translatable("seamless_loading_screen.server.allowCustomScreenshot"))
                .append(": ")
                .append(Text.translatable(
                        "seamless_loading_screen.tinyconfig.boolean."
                                + (((ServerInfoExtras)server).getAllowCustomScreenshot() ? "true" : "false")));
    }

}
