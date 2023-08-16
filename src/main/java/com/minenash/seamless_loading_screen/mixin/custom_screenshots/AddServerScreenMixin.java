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
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AddServerScreen.class)
public abstract class AddServerScreenMixin extends Screen {

    @Shadow @Final private ServerInfo server;

    private ButtonWidget buttonAllowCustomScreenshot;

    protected AddServerScreenMixin(Text title) { super(title); }

    @ModifyArgs(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)V", ordinal = 0))
    private void adjust_addButton_args(Args args){
        args.set(0, ((int) args.get(0)) + 103); // x + 103
        args.set(1, ((int) args.get(1)) + 24);  // y + 24
        args.set(2, ((int) args.get(2)) - 103); // width - 103
    }

    @ModifyArgs(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)V", ordinal = 1))
    private void adjust_cancelButton_args(Args args){
        args.set(2, ((int) args.get(2)) - 103); // width - 103
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void buttonAllowCustomScreenshot(CallbackInfo info) {
        buttonAllowCustomScreenshot = addDrawableChild(new ButtonWidget(width / 2 - 100, height / 4 + 72 + 24, 200, 20,
                getText(), buttonWidget -> {
            ((ServerInfoExtras)server).setAllowCustomScreenshots(!((ServerInfoExtras)server).getAllowCustomScreenshot());
            buttonAllowCustomScreenshot.setMessage(getText());
        }));
    }

    private Text getText() {
        return (Text.translatable("seamless_loading_screen.server.allowCustomScreenshot"))
                .append(": ")
                .append(Text.translatable(
                        "seamless_loading_screen.tinyconfig.boolean."
                                + (((ServerInfoExtras)server).getAllowCustomScreenshot() ? "true" : "false")));
    }

}
