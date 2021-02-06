package com.minenash.seamless_loading_screen;

import com.minenash.seamless_loading_screen.config.Config;
import com.minenash.seamless_loading_screen.mixin.WindowAccessor;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.realms.gui.screen.RealmsBridgeScreen;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FinishQuit extends Screen {

    public FinishQuit() {
        super(new TranslatableText("connect.joining"));
    }

    private static boolean hudHidden = false;
    private static int width, height = 0;


    public static void run() {
        MinecraftClient client = MinecraftClient.getInstance();

        hudHidden = client.options.hudHidden;
        client.options.hudHidden = true;

        if(Config.resolution != Config.ScreenshotResolution.Native) {
            width = client.getWindow().getWidth();
            height = client.getWindow().getHeight();
            resizeScreen(client, Config.resolution.width, Config.resolution.height);
        }
        client.openScreen(new FinishQuit());
    }

    @Override
    public void render(MatrixStack matrices, int _mouseX, int _mouseY, float _delta) {
        assert client != null;

        String name = ScreenshotLoader.getFileName();
        ScreenshotUtils.saveScreenshot(client.runDirectory, name,
                client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight(), client.getFramebuffer(), (text) -> {});

        //Why not just copy the file? Because I tried and the string manipulation was just... oof
        if (Config.archiveScreenshots) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
            ScreenshotUtils.saveScreenshot(client.runDirectory, "worlds/archive/" + name.substring(name.lastIndexOf("/"), name.lastIndexOf(".")) + "_" + timestamp + ".png",
                    client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight(), client.getFramebuffer(), (text) -> {});
        }



        client.options.hudHidden = hudHidden;
        if(Config.resolution != Config.ScreenshotResolution.Native)
            resizeScreen(client, width, height);

        quit(client);

    }

    private static void resizeScreen(MinecraftClient client, int width, int height) {
        client.getFramebuffer().resize(width, height, true);

        WindowAccessor accessor = (WindowAccessor) (Object) client.getWindow();

        accessor.setWidth(width);
        accessor.setHeight(height);
        accessor.setFramebufferWidth(width);
        accessor.setFramebufferHeight(height);

        client.onResolutionChanged();
    }

    private void quit(MinecraftClient client) {
        boolean isSinglePlayer = client.isInSingleplayer();
        boolean isRealms = client.isConnectedToRealms();

        SeamlessLoadingScreen.isDisconnecting = true;
        client.world.disconnect();
        if (isSinglePlayer)
            client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
        else
            client.disconnect();

        if (isRealms) {
            RealmsBridgeScreen realmsBridgeScreen = new RealmsBridgeScreen();
            realmsBridgeScreen.switchToRealms(new TitleScreen());
        }
        else if (isSinglePlayer)
            client.openScreen(new TitleScreen());
        else
            client.openScreen(new MultiplayerScreen(new TitleScreen()));
    }
}
