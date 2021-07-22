package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.Config;
import com.minenash.seamless_loading_screen.mixin.WindowAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FinishQuit extends Screen {

    public FinishQuit() {
        super(new TranslatableText("connect.joining"));
    }

    private static boolean hudHidden = false;
    private static boolean stop = false;
    private static ClientPlayNetworkHandler serverOrderedDisconnectHandler = null;
    private static Text serverOrderedDisconnectReason = null;
    private static int prevWidth, prevHeight;

    public static void run(ClientPlayNetworkHandler screen, Text reason) {
        FinishQuit.serverOrderedDisconnectHandler = screen;
        FinishQuit.serverOrderedDisconnectReason = reason;
        run(false);
    }

    public static void run(boolean stop) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (ScreenshotLoader.allowCustomScreenshot) {
            ScreenshotLoader.allowCustomScreenshot = false;
            if (stop)
                client.scheduleStop();
            else if (serverOrderedDisconnectHandler != null)
                serverOrderedDisconnectHandler.onDisconnected(serverOrderedDisconnectReason);
            else
                quit(client);
            return;
        }

        hudHidden = client.options.hudHidden;
        client.options.hudHidden = true;
        client.options.debugEnabled = false;
        FinishQuit.stop = stop;

        if(Config.resolution != Config.ScreenshotResolution.Native) {
            prevWidth = client.getWindow().getFramebufferWidth();
            prevHeight = client.getWindow().getFramebufferHeight();
            resizeScreen(client, Config.resolution.width, Config.resolution.height);
        }
        client.setScreen(new FinishQuit());
    }

    @Override
    public void render(MatrixStack matrices, int _mouseX, int _mouseY, float _delta) {
        assert client != null;

        String name = ScreenshotLoader.getFileName();

        NativeImage nativeImage = ScreenshotRecorder.takeScreenshot(client.getFramebuffer()); //width and height args do nothing

        try {
            nativeImage.writeFile(new File(name));
            if (Config.archiveScreenshots)
                nativeImage.writeFile(new File("screenshots/worlds/archive/" + name.substring(name.lastIndexOf("/"), name.length()-4) + "_" +  new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Config.updateWorldIcon && client.isInSingleplayer())
            updateIcon(client.getServer().getIconFile().get().toFile(), nativeImage);


        client.options.hudHidden = hudHidden;
        if(Config.resolution != Config.ScreenshotResolution.Native)
            resizeScreen(client, prevWidth, prevHeight);

        SeamlessLoadingScreen.isDisconnecting = true;  //Fapi 0.30.0 compat

        if (stop)
            client.scheduleStop();
        else if (serverOrderedDisconnectHandler != null)
            serverOrderedDisconnectHandler.onDisconnected(serverOrderedDisconnectReason);
        else
            quit(client);

    }

    private static void resizeScreen(MinecraftClient client, int width, int height) {
        Window window = client.getWindow();

        ((WindowAccessor)(Object)window).setFramebufferWidth(width);
        ((WindowAccessor)(Object)window).setFramebufferHeight(height);

        client.onResolutionChanged();
    }

    private static void quit(MinecraftClient client) {
        boolean isSinglePlayer = client.isInSingleplayer();
        boolean isRealms = client.isConnectedToRealms();

        client.world.disconnect();
        if (isSinglePlayer)
            client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
        else
            client.disconnect();

        if (isRealms)
            client.setScreen(new RealmsMainScreen(new TitleScreen()));
        else if (isSinglePlayer)
            client.setScreen(new TitleScreen());
        else
            client.setScreen(new MultiplayerScreen(new TitleScreen()));
    }

    private static void updateIcon(File iconFile, NativeImage nativeImage) {
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
                nativeImage2.writeFile(iconFile);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                nativeImage.close();
            }

        });
    }
}
