package com.minenash.seamless_loading_screen.compat.distant_horizons;

import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import com.seibel.distanthorizons.api.methods.events.DhApiEventRegister;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiAfterEveryInitialRenderSectionRenderedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public class SeamlessLoadingScreenDhRegistry {
	public static void register() {
		if (!FabricLoader.getInstance().isModLoaded(SeamlessLoadingScreen.DISTANT_HORIZONS_MOD_ID)) {
			// The game will most likely crash at this point due to classloading, try to bail
			return;
		}

		DhApiEventRegister.on(DhApiAfterEveryInitialRenderSectionRenderedEvent.class, new SeamlessLoadingScreenDhApiAfterEveryInitialRenderSectionRenderedEvent());
	}
}
