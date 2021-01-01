package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
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
            time = ConfigManager.time;
            timeDelta = 1F / ConfigManager.fade;
        } catch (IOException ignored) {}
    }

    public static void render(Screen screen, MatrixStack stack) {
        if (loaded) {
            MinecraftClient.getInstance().getTextureManager().bindTexture(ScreenshotLoader.SCREENSHOT);

            int w = (int) (imageRatio * screen.height);
            DrawableHelper.drawTexture(stack, screen.width / 2 - w / 2, 0, 0.0F, 0.0F, w, screen.height, w, screen.height);
        }
    }
	
}
