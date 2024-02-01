package com.minenash.seamless_loading_screen.compat.distant_horizons;

import com.minenash.seamless_loading_screen.FadeScreen;
import com.minenash.seamless_loading_screen.ScreenshotLoader;
import com.minenash.seamless_loading_screen.config.SeamlessLoadingScreenConfig;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiAfterEveryInitialRenderSectionRenderedEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiEventParam;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;

@Environment(EnvType.CLIENT)
public class SeamlessLoadingScreenDhApiAfterEveryInitialRenderSectionRenderedEvent extends DhApiAfterEveryInitialRenderSectionRenderedEvent {
	@Override
	public void afterEveryInitialRenderSectionRendered(DhApiEventParam<Void> dhApiEventParam) {
		var minecraftClient = MinecraftClient.getInstance();
		minecraftClient.execute(() -> {
			var currentScreen = minecraftClient.currentScreen;
			var world = minecraftClient.world;

			if (currentScreen instanceof DownloadingTerrainScreen && world != null && ScreenshotLoader.loaded && ScreenshotLoader.inFade) {
				currentScreen.close();

				minecraftClient.setScreen(
						new FadeScreen(SeamlessLoadingScreenConfig.get().time, SeamlessLoadingScreenConfig.get().fade).then((forced) -> {
							if (!forced) minecraftClient.setScreen(null);
							ScreenshotLoader.inFade = false;
						}));

				ScreenshotLoader.replacebg = false;
			}
		});
	}
}
