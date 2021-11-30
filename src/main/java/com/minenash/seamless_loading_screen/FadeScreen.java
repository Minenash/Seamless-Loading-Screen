package com.minenash.seamless_loading_screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Matrix4f;

public class FadeScreen extends Screen {
    private final int fadeFrames;
    private int frames;
    private Runnable callback;
    private boolean done;

    public FadeScreen(int totalFrames, int fadeFrames) {
        super(new TranslatableText("seamless_loading_screen.screen.loading_chunks"));
        this.fadeFrames = Math.min(totalFrames, fadeFrames);
        this.frames = totalFrames;
    }

    public FadeScreen then(Runnable callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void removed() {
        if(done) return;
        done = true;
        this.frames = 0;
        if(callback != null) callback.run();
        else if(client != null) client.setScreen(null);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        if (frames > 0) {
            boolean doFade = frames <= fadeFrames;
            float alpha = doFade ? (float) frames / fadeFrames : 1.0f;

            if(ScreenshotLoader.loaded) {
                RenderSystem.setShaderColor(1, 1, 1, alpha);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                RenderSystem.setShaderTexture(0, ScreenshotLoader.SCREENSHOT);
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
                int w = (int) (ScreenshotLoader.imageRatio * height);
                int h = height;
                int x = width / 2 - w / 2;
                int y = 0;

                Matrix4f modelMat = stack.peek().getModel();
                bufferBuilder.vertex(modelMat, x, y + h, 0).texture(0, 1).color(255, 255, 255, 255).next();
                bufferBuilder.vertex(modelMat, x + w, y + h, 0).texture(1, 1).color(255, 255, 255, 255).next();
                bufferBuilder.vertex(modelMat, x + w, y, 0).texture(1, 0).color(255, 255, 255, 255).next();
                bufferBuilder.vertex(modelMat, x, y, 0).texture(0, 0).color(255, 255, 255, 255).next();
                tessellator.draw();
            }

            if (!doFade) {
                DrawableHelper.drawCenteredText(stack, client.textRenderer, title, width / 2, 70, 0xFFFFFF);
                String progress = String.valueOf(client.worldRenderer.getCompletedChunkCount());
                DrawableHelper.drawCenteredText(stack, client.textRenderer, progress, width / 2, 90, 0xFFFFFF);
            }

            frames--;
            if(frames == 0) {
                removed();
            }
        }
    }
}
