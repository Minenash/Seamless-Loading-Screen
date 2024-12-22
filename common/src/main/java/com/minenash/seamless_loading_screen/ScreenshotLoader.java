package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.SeamlessLoadingScreenConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import static net.minecraft.client.render.DefaultFramebufferSet.MAIN_ONLY;

public class ScreenshotLoader {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Pattern RESERVED_FILENAMES_PATTERN = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", Pattern.CASE_INSENSITIVE);
    public static Identifier SCREENSHOT = Identifier.of(SeamlessLoadingScreen.MODID, "screenshot");
    public static double imageRatio = 1;
    public static boolean loaded = false;
    public static DisplayMode displayMode = DisplayMode.ENABLED;
    public static boolean inFade = false;
    public static int time;
    public static float timeDelta;
    public static boolean replacebg = false;
    private static String fileName = "";

    public static void endBackgroundReplacment() {
        replacebg = false;
    }

    public static boolean shouldReplaceBackground() {
        return replacebg;
    }

    public static String getFileName() {
        return fileName;
    }

    private static void setFileName(String newFileName) {
        var session = MinecraftClient.getInstance().getSession();

        var offlineUUID = Uuids.getOfflinePlayerUuid(session.getUsername());
        var sessionUUID = session.getUuidOrNull();

        var baseFileDir = "screenshots/worlds/";

        if(SeamlessLoadingScreenConfig.get().saveScreenshotsByUsername) {
            var userDir = (sessionUUID != null && !sessionUUID.equals(offlineUUID))
                    ? cleanFileName(session.getUsername())
                    : "offline";

            baseFileDir = "screenshots/" + userDir + "/worlds/";
        }

        fileName = baseFileDir + newFileName;
        setScreenshot();
    }

    public static void setScreenshot(String address, int port) {
        setFileName("screenshots/worlds/servers/" + cleanFileName(address) + "_" + port + ".png");
    }

    public static void setScreenshot(String worldName) {
        setFileName("screenshots/worlds/singleplayer/" + worldName + ".png");
    }

    public static void setRealmScreenshot(String realmName) {
        setFileName("screenshots/worlds/realms/" + cleanFileName(realmName) + ".png");
    }

    private static void setScreenshot() {
        loaded = false;

        if (displayMode == DisplayMode.DISABLED) return;

        try (InputStream in = new FileInputStream(ScreenshotLoader.fileName)) {
            if (PlatformFunctions.isDevEnv()) {
                LOGGER.info("Name: " + ScreenshotLoader.fileName);
            }

            NativeImageBackedTexture image = new NativeImageBackedTexture(NativeImage.read(in));
            MinecraftClient.getInstance().getTextureManager().registerTexture(SCREENSHOT, image);
            imageRatio = image.getImage().getWidth() / (double) image.getImage().getHeight();
            loaded = true;
            time = SeamlessLoadingScreenConfig.get().time;
            timeDelta = 1F / SeamlessLoadingScreenConfig.get().fade;
            replacebg = true;
        } catch (FileNotFoundException ignore) {
        } catch (IOException e) {
            LOGGER.error("[SeamlessLoadingScreen]: An Issue has occurred when attempting to set a Screenshot: [name: {}]", ScreenshotLoader.fileName);
            LOGGER.error(String.valueOf(e));
        }
    }

    private static String cleanFileName(String fileName) {
        for (char c : SharedConstants.INVALID_CHARS_LEVEL_NAME) fileName = fileName.replace(c, '_');

        if (RESERVED_FILENAMES_PATTERN.matcher(fileName).matches()) fileName = "_" + fileName + "_";

        if (fileName.length() > 255 - 4) fileName = fileName.substring(0, 255 - 4);

        return fileName;
    }

    public static void render(Screen screen, DrawContext context) {
        RenderSystem.enableBlend();

        int w = (int) (imageRatio * screen.height);
        context.drawTexture(RenderLayer::getGuiTextured, SCREENSHOT, screen.width / 2 - w / 2, 0, 0.0F, 0.0F, w, screen.height, w, screen.height);

        renderAfterEffects(screen, context, 1f);
        RenderSystem.disableBlend();
    }

    public static void renderAfterEffects(Screen screen, DrawContext context, float fadeValue) {
        renderTint(screen, context, fadeValue);

        if (SeamlessLoadingScreenConfig.get().enableScreenshotBlur) {
            renderBlur(SeamlessLoadingScreenConfig.get().screenshotBlurQuality);
        }
    }

    public static void renderTint(Screen screen, DrawContext context, float fadeValue) {
        Color color = SeamlessLoadingScreenConfig.get().tintColor;

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = Math.round(255 * (SeamlessLoadingScreenConfig.get().tintStrength * fadeValue));

        int argb_color = getArgb(alpha, red, green, blue);

        context.fill(0, 0, screen.width, screen.height, argb_color);
    }

    public static int getArgb(int alpha, int red, int green, int blue) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    //-----

    public static void renderBlur(float quality) {
        var client = MinecraftClient.getInstance();
        PostEffectProcessor postEffectProcessor = client.getShaderLoader().loadPostEffect(Identifier.ofVanilla("blur"), MAIN_ONLY);
        if (postEffectProcessor != null) {
            postEffectProcessor.setUniforms("Radius", quality);
            postEffectProcessor.render(client.getFramebuffer(), client.gameRenderer.pool);
        }
    }
}
