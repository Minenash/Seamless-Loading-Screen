package com.minenash.seamless_loading_screen.forge;

import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = SeamlessLoadingScreen.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SeamlessLoadingScreenShaderInit {

    public static Logger LOGGER = LogUtils.getLogger();

//    public static void init(IEventBus bus){
//        bus.addListener(SeamlessLoadingScreenShaderInit::registerShaders);
//    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event){
        try {
            event.registerShader(new ShaderProgram(event.getResourceProvider(), new Identifier(SeamlessLoadingScreen.MODID, "blur"), VertexFormats.POSITION), (shaderProgram) -> {
                SeamlessLoadingScreen.BLUR_PROGRAM.load(shaderProgram);
            });
        } catch (IOException e){
            LOGGER.error("[SeamlessLoadingScreenShaderInit] An issue with loading the needed shader files has failed, blur will not be working!", e);
        }
    }
}
