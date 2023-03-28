package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class ScreenshotLoader {

    public static Identifier SCREENSHOT = new Identifier("visualconnect", "screenshot");
    public static double imageRatio = 1;
    public static boolean loaded = false;
    public static boolean allowCustomScreenshot = false;

    public static boolean inFade = false;
    public static int time;
    public static float timeDelta;

    private static String fileName = "";

    public static String getFileName() {
        return fileName;
    }

	public static void setScreenshot(String address, int port) {
        fileName = "screenshots/worlds/servers/" + address + "_" + port + ".png";
        setScreenshot();
    }

    public static void setScreenshot(String worldName) {
        fileName = "screenshots/worlds/singleplayer/" + worldName + ".png";
        setScreenshot();
    }

    public static void setRealmScreenshot(String realmName) {
        fileName = "screenshots/worlds/realms/" + cleanFileName(realmName) + ".png";
        setScreenshot();
    }

    private static void setScreenshot() {
        loaded = false;
        try (InputStream in = new FileInputStream(ScreenshotLoader.fileName)) {
            System.out.println("Name: " + ScreenshotLoader.fileName);
            NativeImageBackedTexture image = new NativeImageBackedTexture(NativeImage.read(in));
            MinecraftClient.getInstance().getTextureManager().registerTexture(SCREENSHOT, image);
            imageRatio = image.getImage().getWidth() / (double) image.getImage().getHeight();
            loaded = true;
            time = Config.time;
            timeDelta = 1F / Config.fade;
        } catch (IOException ignored) {}
    }

    private static final Pattern RESERVED_FILENAMES_PATTERN = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", Pattern.CASE_INSENSITIVE);
    private static String cleanFileName(String fileName) {
        for (char c : SharedConstants.INVALID_CHARS_LEVEL_NAME)
            fileName = fileName.replace(c, '_');

        if (RESERVED_FILENAMES_PATTERN.matcher(fileName).matches())
            fileName = "_" + fileName + "_";

        if (fileName.length() > 255 - 4)
            fileName = fileName.substring(0, 255 - 4);

        return fileName;
    }

    public static void render(Screen screen, MatrixStack stack) {
        if (loaded) {
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, SCREENSHOT);

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
