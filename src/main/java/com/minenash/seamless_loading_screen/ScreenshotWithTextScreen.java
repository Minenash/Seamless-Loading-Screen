package com.minenash.seamless_loading_screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ScreenshotWithTextScreen extends Screen {

    public ScreenshotWithTextScreen() {
        this(Text.literal(""));
    }
    public ScreenshotWithTextScreen(Text text) {
        super(text);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        ScreenshotLoader.render(this, matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 70, 16777215);

        super.render(matrices, mouseX, mouseY, delta);

    }
}
