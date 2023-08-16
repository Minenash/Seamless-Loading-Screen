package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.Config;
import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.util.Util;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OnQuitHelper {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static boolean attemptScreenShot = false;

    public static Runnable onceFinished = () -> {};

    private static int old_FrameBufferWidth = 0;
    private static int old_FrameBufferHeight = 0;

    public static void beginScreenshotTask(Runnable runnable){
        //ScreenshotLoader.replacebg = false;

        attemptScreenShot = true;

        onceFinished = runnable;

        var window = MinecraftClient.getInstance().getWindow();

        old_FrameBufferWidth = window.getFramebufferWidth();
        old_FrameBufferHeight = window.getFramebufferHeight();

        window.setFramebufferWidth(Config.resolution.width);
        window.setFramebufferHeight(Config.resolution.height);

        MinecraftClient.getInstance().onResolutionChanged();
    }

    public static void takeScreenShot(){
        var client = MinecraftClient.getInstance();

        String name = ScreenshotLoader.getFileName();

        NativeImage nativeImage = ScreenshotRecorder.takeScreenshot(client.getFramebuffer()); //width and height args do nothing

        try {
            File file = new File(name);

            if(!file.exists()) file.createNewFile();

            nativeImage.writeTo(new File(name));

            if (Config.archiveScreenshots) {
                String fileName = "screenshots/worlds/archive/" + name.substring(name.lastIndexOf("/"), name.length() - 4) + "_" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".png";

                File archiveFile = new File(fileName);

               if(!archiveFile.exists()) archiveFile.createNewFile();

                nativeImage.writeTo(archiveFile);
            }
        } catch (IOException e) {
            LOGGER.error("[SeamlessLoadingScreen]: Unable to take a screenshot on leaving of a world, such will not be saved! [Name: {}]", name);
            LOGGER.error(e.toString());
        }

        if (Config.updateWorldIcon && client.isInSingleplayer()) updateIcon(client.getServer().getIconFile().get().toFile(), nativeImage);

        attemptScreenShot = false;

        var window = MinecraftClient.getInstance().getWindow();

        window.setFramebufferWidth(old_FrameBufferWidth);
        window.setFramebufferHeight(old_FrameBufferHeight);

        client.onResolutionChanged();

        finished();
    }

    public static void updateIcon(File iconFile, NativeImage nativeImage) {
        Util.getIoWorkerExecutor().execute(() -> {
            int i = nativeImage.getWidth();
            int j = nativeImage.getHeight();
            int k = 0;
            int l = 0;
            if (i > j) {
                k = (i - j) / 2;
                i = j;
            } else {
                l = (j - i) / 2;
                j = i;
            }

            try(NativeImage nativeImage2 = new NativeImage(64, 64, false)) {
                nativeImage.resizeSubRectTo(k, l, i, j, nativeImage2);
                nativeImage2.writeTo(iconFile);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                nativeImage.close();
            }

        });
    }

    public static void finished(){
        onceFinished.run();

        onceFinished = () -> {};
    }
}
