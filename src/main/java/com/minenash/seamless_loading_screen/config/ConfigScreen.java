package com.minenash.seamless_loading_screen.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ConfigScreen extends Screen {

    private final Screen parent;
    private final MinecraftClient client;

    private TextFieldWidget timeField;
    private TextFieldWidget fadeField;

    private static final int OPTION_START = 32+13, OPTION_BUFFER = 30;

    private boolean disableCamera = ConfigManager.disableCamera;

    private boolean disableHRImage = ConfigManager.disableHRImage;

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

        timeField = new TextFieldWidget(textRenderer, buttonX, getY(0), buttonWidth, buttonHeight, null);
        timeField.setMaxLength(3);
        timeField.setText(Integer.toString(ConfigManager.time));
        children.add(this.timeField);

        fadeField = new TextFieldWidget(this.textRenderer, buttonX, getY(1), buttonWidth, buttonHeight, null);
        timeField.setMaxLength(3);
        fadeField.setText(Integer.toString(ConfigManager.fade));
        children.add(this.fadeField);

        this.addButton(new ButtonWidget(buttonX,getY(2), buttonWidth, buttonHeight,
                                        getDisableCameraLabel(), (button) -> {
            disableCamera = !disableCamera;
            button.setMessage( getDisableCameraLabel() );
        }));

        this.addButton(new ButtonWidget(buttonX,getY(3), buttonWidth, buttonHeight,
                                        getDisableHRShotLabel(), (button) -> {
            disableHRImage = !disableHRImage;
            button.setMessage( getDisableHRShotLabel() );
        }));

        this.addButton(new ButtonWidget(this.width/2 - 100,this.height - 28,200,20,
                new TranslatableText("gui.done"), (button) -> onClose()));

    }

    private Text getDisableCameraLabel() {
        return new TranslatableText("seamless_loading_screen.config.boolean." + (disableCamera ? "true" : "false"));
    }
    private Text getDisableHRShotLabel() {
        return new TranslatableText("seamless_loading_screen.config.boolean." + (disableHRImage ? "true" : "false"));
    }

    @Override
    public void onClose() {
        System.out.println("Fade: " + fadeField.getText());
        ConfigManager.time = Integer.parseInt(timeField.getText());
        ConfigManager.fade = Integer.parseInt(fadeField.getText());
        ConfigManager.disableCamera = disableCamera;
        ConfigManager.disableHRImage = disableHRImage;
        ConfigManager.saveConfig();
        client.openScreen(parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        int x = 20;


        drawCenteredText(matrices, textRenderer, new TranslatableText("seamless_loading_screen.config.title"), this.width / 2, 13, 0xFFFFFF);
        drawTextWithShadow(matrices, textRenderer, new TranslatableText("seamless_loading_screen.config.delay"), x, getY(0) + 5, 0xFFFFFF);
        drawTextWithShadow(matrices, textRenderer, new TranslatableText("seamless_loading_screen.config.fade_duration"), x, getY(1) + 5, 0xFFFFFF);
        drawTextWithShadow(matrices, textRenderer, new TranslatableText("seamless_loading_screen.config.disable_camera"), x, getY(2) + 5, 0xFFFFFF);
        drawTextWithShadow(matrices, textRenderer, new TranslatableText("seamless_loading_screen.config.disable_hr_image"), x, getY(3) + 5, 0xFFFFFF);

        timeField.render(matrices, mouseX, mouseY, delta);
        fadeField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

    private int getY(int offset) {
        return OPTION_START + OPTION_BUFFER * offset;
    }



}
