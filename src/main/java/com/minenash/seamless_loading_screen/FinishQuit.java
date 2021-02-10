package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.Config;
import com.minenash.seamless_loading_screen.mixin.WindowAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.realms.gui.screen.RealmsBridgeScreen;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.client.util.math.MatrixStack;
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


    public static void run(boolean stop) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (ScreenshotLoader.allowCustomScreenshot) {
            if (stop)
                client.scheduleStop();
            else
                quit(client);
            return;
        }

        hudHidden = client.options.hudHidden;
        client.options.hudHidden = true;
        FinishQuit.stop = stop;

        if(Config.resolution != Config.ScreenshotResolution.Native) {
            resizeScreen(client, Config.resolution.width, Config.resolution.height);
        }
        client.openScreen(new FinishQuit());
    }

    @Override
    public void render(MatrixStack matrices, int _mouseX, int _mouseY, float _delta) {
        assert client != null;

        String name = ScreenshotLoader.getFileName();

        NativeImage nativeImage = ScreenshotUtils.takeScreenshot(0, 0, client.getFramebuffer()); //width and height args do nothing

        try {
            nativeImage.writeFile(new File(name));
            if (Config.archiveScreenshots)
                nativeImage.writeFile(new File("screenshots/worlds/archive/" + name.substring(name.lastIndexOf("/"), name.length()-4) + "_" +  new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Config.updateWorldIcon && client.isInSingleplayer())
            updateIcon(client.getServer().getIconFile(), nativeImage);


        client.options.hudHidden = hudHidden;
        if(Config.resolution != Config.ScreenshotResolution.Native)
            resizeScreen(client, client.getWindow().getWidth(), client.getWindow().getHeight());

        if (stop)
            client.scheduleStop();
        else
            quit(client);

    }

    private static void resizeScreen(MinecraftClient client, int width, int height) {
        WindowAccessor window = (WindowAccessor) (Object) client.getWindow();

        window.setFramebufferWidth(width);
        window.setFramebufferHeight(height);

        client.onResolutionChanged();
    }

    private static void quit(MinecraftClient client) {
        boolean isSinglePlayer = client.isInSingleplayer();
        boolean isRealms = client.isConnectedToRealms();

        SeamlessLoadingScreen.isDisconnecting = true;  //Fapi 0.30.0 compat
        client.world.disconnect();
        if (isSinglePlayer)
            client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
        else
            client.disconnect();

        if (isRealms)
            client.openScreen(new RealmsMainScreen(new TitleScreen()));
        else if (isSinglePlayer)
            client.openScreen(new TitleScreen());
        else
            client.openScreen(new MultiplayerScreen(new TitleScreen()));
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
