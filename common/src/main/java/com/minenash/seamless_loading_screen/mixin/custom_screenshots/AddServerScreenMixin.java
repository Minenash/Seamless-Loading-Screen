package com.minenash.seamless_loading_screen.mixin.custom_screenshots;

import com.minenash.seamless_loading_screen.ServerInfoExtension;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AddServerScreen.class)
public abstract class AddServerScreenMixin extends Screen {

    @Shadow @Final private ServerInfo server;

    @Unique private ButtonWidget buttonDisplayMode;

    protected AddServerScreenMixin(Text title) { super(title); }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)V", ordinal = 0), index = 0)
    private int adjust_addButton_x(int x){
        return x + 103; // x + 103
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)V", ordinal = 0), index = 1)
    private int adjust_addButton_y(int y){
        return y + 24; // y + 24
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)V", ordinal = 0), index = 2)
    private int adjust_addButton_width(int width){
        return width - 103; // width - 103
    }


    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)V", ordinal = 1), index = 2)
    private int adjust_cancelButton_wdith(int width){
        return width - 103; // width - 103
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void buttonAllowCustomScreenshot(CallbackInfo info) {
        buttonDisplayMode = addDrawableChild(new ButtonWidget(width / 2 - 100, height / 4 + 72 + 24, 200, 20,
                getText(), buttonWidget -> {
            ((ServerInfoExtension)server).setDisplayMode(((ServerInfoExtension)server).getDisplayMode().next());
            buttonDisplayMode.setMessage(getText());
        }));
    }

    private Text getText() {
        return (Text.translatable("seamless_loading_screen.server.displayMode"))
                .append(": ")
                .append(Text.translatable(
                        "seamless_loading_screen.server.displayMode."
                                + (((ServerInfoExtension)server).getDisplayMode().toString())));
    }

}
