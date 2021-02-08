package com.minenash.seamless_loading_screen.mixin.customScreenshots;

import com.minenash.seamless_loading_screen.ServerInfoExtras;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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

    @Redirect(method = "init", at = @At(value = "NEW", target = "net/minecraft/client/gui/widget/ButtonWidget", ordinal = 1))
    private ButtonWidget buttonAdd(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress) {
        return new ButtonWidget(x+103,y+24,width-103,height,message,onPress);
    }

    @Redirect(method = "init", at = @At(value = "NEW", target = "net/minecraft/client/gui/widget/ButtonWidget", ordinal = 2))
    private ButtonWidget buttonCancel(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress) {
        return new ButtonWidget(x,y,width-103,height,message,onPress);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void buttonAllowCustomScreenshot(CallbackInfo info) {
        buttonAllowCustomScreenshot = addButton(new ButtonWidget(width / 2 - 100, height / 4 + 72 + 24, 200, 20,
                getText(), buttonWidget -> {
            ((ServerInfoExtras)server).setAllowCustomScreenshots(!((ServerInfoExtras)server).getAllowCustomScreenshot());
            buttonAllowCustomScreenshot.setMessage(getText());
        }));
    }

    private Text getText() {
        return (new TranslatableText("seamless_loading_screen.server.allowCustomScreenshot"))
                .append(": ")
                .append(new TranslatableText(
                        "seamless_loading_screen.tinyconfig.boolean."
                                + (((ServerInfoExtras)server).getAllowCustomScreenshot() ? "true" : "false")));
    }

}
