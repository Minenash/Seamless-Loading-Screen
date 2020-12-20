package com.minenash.visual_connect.mixin;


import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Window.class)
public interface WindowAccessor {

    @Accessor
    void setWidth(int width);

    @Accessor
    void setHeight(int height);

    @Accessor
    void setFramebufferWidth(int framebufferWidth);

    @Accessor
    void setFramebufferHeight(int framebufferHeight);
}