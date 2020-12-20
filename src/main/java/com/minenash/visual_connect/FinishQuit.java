package com.minenash.visual_connect;

import com.minenash.visual_connect.mixin.WindowAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.realms.gui.screen.RealmsBridgeScreen;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

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

        width = client.getWindow().getWidth();
        height = client.getWindow().getHeight();
        resizeScreen(client, 3440, 1440);

        client.openScreen(new FinishQuit());
    }

    @Override
    public void render(MatrixStack matrices, int _mouseX, int _mouseY, float _delta) {
        assert client != null;

        ScreenshotUtils.saveScreenshot(client.runDirectory, ScreenshotLoader.getFileName(),
            client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight(), client.getFramebuffer(), (text) -> {});

        client.options.hudHidden = hudHidden;
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
