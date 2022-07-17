package com.minenash.seamless_loading_screen.mixin.mod_compat;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ModCompatMixinPlugin implements IMixinConfigPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String[] packages = mixinClassName.split("\\.");
        if (packages.length <= 5)
            return true;

        switch (packages[5]) {
            case "bedrockify": return FabricLoader.getInstance().isModLoaded("bedrockify");
            case "fapi_0_30_0": {
                ModContainer mod = FabricLoader.getInstance().getModContainer("fabric").orElse(null);
                return mod != null && Integer.parseInt(mod.getMetadata().getVersion().getFriendlyString().split("\\.")[1]) >= 30;
            }
        }
        return true;
    }

    @Override
    public void onLoad(String mixinPackage) { }

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
}
