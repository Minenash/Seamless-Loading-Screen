package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotLoader;
import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import com.minenash.seamless_loading_screen.config.SeamlessLoadingScreenConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreenMixin {
	@Shadow
	@Final
	@Mutable
	private static Text TEXT;

	@Unique
	private boolean seamless_loading_screen$changedTextToDhLodLoadingText = false;

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/DownloadingTerrainScreen;close()V"), cancellable = true)
	private void stopBackgroundReplacement(CallbackInfo ci) {
		// Cancel background replacement stopping, as this should be handled via a Distant Horizons event when Distant Horizons is loaded
		if (FabricLoader.getInstance().isModLoaded(SeamlessLoadingScreen.DISTANT_HORIZONS_MOD_ID) && SeamlessLoadingScreenConfig.get().distantHorizonsCompat) {
			if (!seamless_loading_screen$changedTextToDhLodLoadingText) {
				TEXT = Text.translatable("seamless_loading_screen.screen.loading_lods");
				seamless_loading_screen$changedTextToDhLodLoadingText = true;
			}

			ci.cancel();
			return;
		}

		ScreenshotLoader.replacebg = false;
	}
}
