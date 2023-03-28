package com.minenash.seamless_loading_screen.mixin;

import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Window.class)
public interface WindowAccessor {
    @Accessor void setFramebufferWidth(int framebufferWidth);
    @Accessor void setFramebufferHeight(int framebufferHeight);
}
