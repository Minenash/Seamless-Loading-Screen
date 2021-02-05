package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.Config;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ScreenshotLoader {

    public static Identifier SCREENSHOT = new Identifier("visualconnect", "screenshot");
    public static double imageRatio = 1;
    public static boolean loaded = false;

    public static boolean inFade = false;
    public static int time;
    public static float timeDelta;

    private static String fileName = "";

    public static String getFileName() {
        return fileName;
    }

	public static void setScreenshot(String address, int port) {
        fileName = "worlds/servers/" + address + "_" + port + ".png";
        setScreenshot();
    }

    public static void setScreenshot(String worldName) {
        fileName = "worlds/singleplayer/" + worldName + ".png";
        setScreenshot();
    }

    private static void setScreenshot() {
        loaded = false;
        try (InputStream in = new FileInputStream("screenshots/" + ScreenshotLoader.fileName)) {
            NativeImageBackedTexture image = new NativeImageBackedTexture(NativeImage.read(in));
            MinecraftClient.getInstance().getTextureManager().registerTexture(SCREENSHOT, image);
            imageRatio = image.getImage().getWidth() / (double) image.getImage().getHeight();
            loaded = true;
            time = Config.time;
            timeDelta = 1F / Config.fade;
        } catch (IOException ignored) {}
    }

    public static void render(Screen screen, MatrixStack stack) {
        if (loaded) {
            MinecraftClient.getInstance().getTextureManager().bindTexture(ScreenshotLoader.SCREENSHOT);

            int w = (int) (imageRatio * screen.height);
            DrawableHelper.drawTexture(stack, screen.width / 2 - w / 2, 0, 0.0F, 0.0F, w, screen.height, w, screen.height);
           // renderVignette();
        }
    }

//    public static void renderVignette() {
//        MinecraftClient client = MinecraftClient.getInstance();
//
//        int scaledWidth = client.getWindow().getScaledWidth();
//        int scaledHeight = client.getWindow().getScaledHeight();
//
//        RenderSystem.enableBlend();
//        RenderSystem.disableDepthTest();
//        RenderSystem.depthMask(false);
//        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
//        RenderSystem.color4f(0.5F, 0.5F, 0.5F, 1.0F);
//
//        client.getTextureManager().bindTexture(new Identifier("textures/misc/vignette.png"));
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bufferBuilder = tessellator.getBuffer();
//        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
//        bufferBuilder.vertex(0.0D, scaledHeight, -90.0D).texture(0.0F, 1.0F).next();
//        bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0D).texture(1.0F, 1.0F).next();
//        bufferBuilder.vertex(scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
//        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
//        tessellator.draw();
//        RenderSystem.depthMask(true);
//        RenderSystem.enableDepthTest();
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.defaultBlendFunc();
//    }
	
}
