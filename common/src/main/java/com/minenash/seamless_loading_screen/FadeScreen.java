package com.minenash.seamless_loading_screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vector4f;

import java.util.function.Consumer;

public class FadeScreen extends Screen {
    private final int fadeFrames;
    private int frames;
    private Consumer<Boolean> callback;
    private boolean done;

    public FadeScreen(int totalFrames, int fadeFrames) {
        super(Text.translatable("seamless_loading_screen.screen.loading_chunks"));
        this.fadeFrames = Math.min(totalFrames, fadeFrames);
        this.frames = totalFrames;
    }

    public FadeScreen then(Consumer<Boolean> callback) {
        this.callback = callback;

        return this;
    }

    @Override
    public void removed() {
        markDone(true);

        super.removed();
    }

    private void markDone(boolean forceClosed) {
        if(this.done) return;

        this.done = true;

        this.frames = 0;

        if(this.callback != null) this.callback.accept(forceClosed);
        if(this.callback == null && !forceClosed && this.client != null && this.client.currentScreen == this) this.client.setScreen(null);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        if (frames <= 0 || client == null) return;

        boolean doFade = frames <= fadeFrames;

        float alpha = doFade ? (float) frames / fadeFrames : 1.0f;

        Vector4f color = new Vector4f(1, 1, 1, alpha);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        if(ScreenshotLoader.loaded) {
            RenderSystem.setShaderTexture(0, ScreenshotLoader.SCREENSHOT);
            int w = (int) (ScreenshotLoader.imageRatio * height);
            int h = height;
            int x = width / 2 - w / 2;
            int y = 0;

            loadQuad(stack, color, x, y, x+w, y+h).draw();

            if(w < width) {
                RenderSystem.setShaderTexture(0, OPTIONS_BACKGROUND_TEXTURE);
                // 0.25f is from Screen.renderBackgroundTexture vertex colors
                color.set(0.25f, 0.25f, 0.25f, alpha);
                loadQuad(stack, color, 0, 0, x, height, 0, 0, x/32f, height/32f).draw();
                loadQuad(stack, color, x+w, 0, width, height, (x+w)/32f, 0, width/32f, height/32f).draw();
            }
        } else {
            RenderSystem.setShaderTexture(0, OPTIONS_BACKGROUND_TEXTURE);
            color.set(0.25f, 0.25f, 0.25f, alpha);
            loadQuad(stack, color, 0, 0, width, height, 0, 0, width/32f, height/32f).draw();
        }

        ScreenshotLoader.renderTint(this, stack, alpha);

        if (!doFade) {
            DrawableHelper.drawCenteredText(stack, client.textRenderer, title, width / 2, 70, 0xFFFFFF);
            String progress = String.valueOf(client.worldRenderer.getCompletedChunkCount());
            DrawableHelper.drawCenteredText(stack, client.textRenderer, progress, width / 2, 90, 0xFFFFFF);
        }

        RenderSystem.disableBlend();

        frames--;

        if(frames == 0) markDone(false);
    }

    private Tessellator loadQuad(MatrixStack stack, Vector4f color, int x0, int y0, int x1, int y1) {
        return loadQuad(stack, color, x0, y0, x1, y1, 0, 0, 1, 1);
    }

    private Tessellator loadQuad(MatrixStack stack, Vector4f color, int x0, int y0, int x1, int y1, float u0, float v0, float u1, float v1) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        Matrix4f modelMat = stack.peek().getPositionMatrix();
        bufferBuilder.vertex(modelMat, x0, y1, 0).texture(u0, v1).color(color.getX(), color.getY(), color.getZ(), color.getW()).next();
        bufferBuilder.vertex(modelMat, x1, y1, 0).texture(u1, v1).color(color.getX(), color.getY(), color.getZ(), color.getW()).next();
        bufferBuilder.vertex(modelMat, x1, y0, 0).texture(u1, v0).color(color.getX(), color.getY(), color.getZ(), color.getW()).next();
        bufferBuilder.vertex(modelMat, x0, y0, 0).texture(u0, v0).color(color.getX(), color.getY(), color.getZ(), color.getW()).next();
        return tessellator;
    }
}