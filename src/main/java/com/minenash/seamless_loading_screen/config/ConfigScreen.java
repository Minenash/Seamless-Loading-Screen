package com.minenash.seamless_loading_screen.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ConfigScreen extends Screen {

    private final Screen parent;
    private final MinecraftClient client;

    private TextFieldWidget timeField;
    private TextFieldWidget fadeField;

    private static final int OPTION_START = 32+13, OPTION_BUFFER = 30;

    private boolean disableCamera = ConfigManager.disableCamera;

    public ConfigScreen(Screen parent) {
        super(new TranslatableText("sml.config.screen.title"));
        this.parent = parent;
        this.client = MinecraftClient.getInstance();
    }

    @Override
    protected void init() {

        int buttonWidth = 75;
        int buttonHeight = 20;
        int buttonX = this.width - buttonWidth - 10;

        timeField = new TextFieldWidget(textRenderer, buttonX, getY(0), buttonWidth, buttonHeight, new LiteralText("Time"));
        timeField.setMaxLength(3);
        timeField.setText(Integer.toString(ConfigManager.time));
        children.add(this.timeField);

        fadeField = new TextFieldWidget(this.textRenderer, buttonX, getY(1), buttonWidth, buttonHeight, new LiteralText("Fade"));
        timeField.setMaxLength(3);
        fadeField.setText(Integer.toString(ConfigManager.fade));
        children.add(this.fadeField);

        this.addButton(new ButtonWidget(buttonX,getY(2), buttonWidth, buttonHeight,
                getDisableCameraLabel(), (button) -> {
            disableCamera = !disableCamera;
            button.setMessage( getDisableCameraLabel() );
        }));

        this.addButton(new ButtonWidget(this.width/2 - 100,this.height - 28,200,20,
                new TranslatableText("gui.done"), (button) -> onClose()));

    }

    private Text getDisableCameraLabel() {
        return new LiteralText(disableCamera ? "True" : "False");
    }

    @Override
    public void onClose() {
        System.out.println("Fade: " + fadeField.getText());
        ConfigManager.time = Integer.parseInt(timeField.getText());
        ConfigManager.fade = Integer.parseInt(fadeField.getText());
        ConfigManager.disableCamera = disableCamera;
        ConfigManager.saveConfig();
        client.openScreen(parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        int x = 20;


        drawCenteredText(matrices, textRenderer, new LiteralText("Options"), this.width / 2, 13, 0xFFFFFF);
        drawTextWithShadow(matrices, textRenderer, new LiteralText("Delay for loading chunks"), x, getY(0), 0xFFFFFF);
        drawTextWithShadow(matrices, textRenderer, new LiteralText("Fade Duration"), x, getY(1), 0xFFFFFF);
        drawTextWithShadow(matrices, textRenderer, new LiteralText("Disable Camera Movement until the fade is done"), x, getY(2), 0xFFFFFF);

        timeField.render(matrices, mouseX, mouseY, delta);
        fadeField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

    private int getY(int offset) {
        return OPTION_START + OPTION_BUFFER * offset + 5;
    }



}
