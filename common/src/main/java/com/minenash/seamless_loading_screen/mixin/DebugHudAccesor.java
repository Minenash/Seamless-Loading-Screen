package com.minenash.seamless_loading_screen.mixin;

import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DebugHud.class)
public interface DebugHudAccesor {

    @Accessor("showDebugHud")
    void seamless$showDebugHud(boolean value);

    @Accessor("renderingChartVisible")
    void seamless$renderingChartVisible(boolean value);

    @Accessor("renderingAndTickChartsVisible")
    void seamless$renderingAndTickChartsVisible(boolean value);
}
