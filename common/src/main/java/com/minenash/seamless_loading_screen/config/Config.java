package com.minenash.seamless_loading_screen.config;

import com.google.gson.GsonBuilder;
import com.minenash.seamless_loading_screen.PlatformFunctions;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.GsonConfigInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.List;

public class Config {
    private static final GsonConfigInstance<Config> GSON = GsonConfigInstance.createBuilder(Config.class)
            .overrideGsonBuilder(
                    new GsonBuilder()
                            .setPrettyPrinting()
                            .disableHtmlEscaping()
                            .serializeNulls()
                            .registerTypeHierarchyAdapter(Text.class, new Text.Serializer())
                            .registerTypeHierarchyAdapter(Style.class, new Style.Serializer())
                            .registerTypeHierarchyAdapter(Color.class, new GsonConfigInstance.ColorTypeAdapter())
            )
            .setPath(PlatformFunctions.getConfigDirectory().resolve("seamless_loading_screen.json"))
            .build();

    public static Config get() {
        return GSON.getConfig();
    }

    public static void load() {
        GSON.load();
    }

    public static void save() {
        GSON.save();
    }

    private static Text getName(String id) {
        return Text.translatable("seamless_loading_screen.config." + id);
    }

    private static Text getDesc(String id) {
        return Text.translatable("seamless_loading_screen.config." + id + ".description");
    }

    public static YetAnotherConfigLib getInstance() {
        return YetAnotherConfigLib.create(GSON,
                (defaults, config, builder) -> {

                    var timeOpt = Option.<Integer>createBuilder()
                            .name(getName("time"))
                            .description(OptionDescription.createBuilder().text(getDesc("time")).build())
                            .binding(defaults.time, () -> config.time, (val) -> config.time = val)
                            .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(0))
                            .build();

                    var fadeOpt = Option.<Integer>createBuilder()
                            .name(getName("fade"))
                            .description(OptionDescription.createBuilder().text(getDesc("fade")).build())
                            .binding(defaults.fade, () -> config.fade, (val) -> config.fade = val)
                            .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(0))
                            .build();

                    var disableCameraOpt = Option.<Boolean>createBuilder()
                            .name(getName("disableCamera"))
                            .description(OptionDescription.createBuilder().text(getDesc("disableCamera")).build())
                            .binding(defaults.disableCamera, () -> config.disableCamera, (val) -> config.disableCamera = val)
                            .controller(opt -> BooleanControllerBuilder.create(opt).trueFalseFormatter())
                            .build();

                    var soundOpt = Option.<String>createBuilder()
                            .name(getName("soundEffect"))
                            .description(OptionDescription.createBuilder().text(getDesc("soundEffect")).build())
                            .binding(defaults.soundEffect, () -> config.soundEffect, (val) -> config.soundEffect = val)
                            .controller(StringControllerBuilder::create)
                            .build();

                    var soundPitchOpt = Option.<Float>createBuilder()
                            .name(getName("soundPitch"))
                            .description(OptionDescription.createBuilder().text(getDesc("soundPitch")).build())
                            .binding(defaults.soundPitch, () -> config.soundPitch, (val) -> config.soundPitch = val)
                            .controller(opt -> FloatFieldControllerBuilder.create(opt).min(0f).max(10f))
                            .build();

                    var soundVolumeOpt = Option.<Float>createBuilder()
                            .name(getName("soundVolume"))
                            .description(OptionDescription.createBuilder().text(getDesc("soundVolume")).build())
                            .binding(defaults.soundVolume, () -> config.soundVolume, (val) -> config.soundVolume = val)
                            .controller(opt -> FloatFieldControllerBuilder.create(opt).min(0f).max(10f))
                            .build();

                    var playSoundEffectOpt = Option.<Boolean>createBuilder()
                            .name(getName("playSoundEffect"))
                            .description(OptionDescription.createBuilder().text(getDesc("playSoundEffect")).build())
                            .binding(defaults.playSoundEffect, () -> config.playSoundEffect, (val) -> config.playSoundEffect = val)
                            .listener((opt, val) -> {
                                soundPitchOpt.setAvailable(val);
                                soundVolumeOpt.setAvailable(val);
                            })
                            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                            .build();

                    var screenshotBlurStrengthOpt = Option.<Float>createBuilder()
                            .name(getName("screenshotBlurStrength"))
                            .description(OptionDescription.createBuilder().text(getDesc("screenshotBlurStrength")).build())
                            .binding(defaults.screenshotBlurStrength, () -> config.screenshotBlurStrength, (val) -> config.screenshotBlurStrength = val)
                            .controller(opt -> FloatSliderControllerBuilder.create(opt).range(1f, 16f).step(0.1f))
                            .build();

                    var screenshotBlurQualityOpt = Option.<Float>createBuilder()
                            .name(getName("screenshotBlurQuality"))
                            .description(OptionDescription.createBuilder().text(getDesc("screenshotBlurQuality")).build())
                            .binding(defaults.screenshotBlurQuality, () -> config.screenshotBlurQuality, (val) -> config.screenshotBlurQuality = val)
                            .controller(opt -> FloatSliderControllerBuilder.create(opt).range(1f, 16f).step(0.1f))
                            .build();

                    var enableScreenshotBlurOpt = Option.<Boolean>createBuilder()
                            .name(getName("enableScreenshotBlur"))
                            .description(OptionDescription.createBuilder().text(getDesc("enableScreenshotBlur")).build())
                            .binding(defaults.enableScreenshotBlur, () -> config.enableScreenshotBlur, (val) -> config.enableScreenshotBlur = val)
                            .listener((opt, val) -> {
                                screenshotBlurQualityOpt.setAvailable(val);
                                screenshotBlurStrengthOpt.setAvailable(val);
                            })
                            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                            .build();

                    var tintColorOpt = Option.<Color>createBuilder()
                            .name(getName("tintColor"))
                            .description(OptionDescription.createBuilder().text(getDesc("tintColor")).build())
                            .binding(defaults.tintColor, () -> config.tintColor, (val) -> config.tintColor = val)
                            .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                            .build();

                    var tintStrengthOpt = Option.<Float>createBuilder()
                            .name(getName("tintStrength"))
                            .description(OptionDescription.createBuilder().text(getDesc("tintStrength")).build())
                            .binding(defaults.tintStrength, () -> config.tintStrength, (val) -> config.tintStrength = val)
                            .controller(opt -> FloatSliderControllerBuilder.create(opt).range(0f, 1f).step(0.05f))
                            .build();

                    var archiveScreenshotsOpt = Option.<Boolean>createBuilder()
                            .name(getName("archiveScreenshots"))
                            .description(OptionDescription.createBuilder().text(getDesc("archiveScreenshots")).build())
                            .binding(defaults.archiveScreenshots, () -> config.archiveScreenshots, (val) -> config.archiveScreenshots = val)
                            .controller(BooleanControllerBuilder::create)
                            .build();

                    var resolutionOpt = Option.<ScreenshotResolution>createBuilder()
                            .name(getName("resolution"))
                            .description(OptionDescription.createBuilder().text(getDesc("resolution")).build())
                            .binding(defaults.resolution, () -> config.resolution, (val) -> config.resolution = val)
                            .controller(opt -> EnumControllerBuilder.create(opt)
                                    .enumClass(ScreenshotResolution.class)
                                    .valueFormatter(val -> Text.translatable("seamless_loading_screen.config.resolution." + val.name().toLowerCase()))
                            ).build();

                    var updateWorldIconOpt = Option.<Boolean>createBuilder()
                            .name(getName("updateWorldIcon"))
                            .description(OptionDescription.createBuilder().text(getDesc("updateWorldIcon")).build())
                            .binding(defaults.updateWorldIcon, () -> config.updateWorldIcon, (val) -> config.updateWorldIcon = val)
                            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter())
                            .build();

                    return builder
                            .title(getName("title"))
                            .category(ConfigCategory.createBuilder()
                                    .name(getName("display"))
                                    .tooltip(getDesc("display"))
                                    .options(List.of(timeOpt, fadeOpt, disableCameraOpt))
                                    .group(OptionGroup.createBuilder().name(getName("soundEffects"))
                                            .options(List.of(playSoundEffectOpt, soundOpt, soundVolumeOpt, soundPitchOpt)).build())
                                    .group(OptionGroup.createBuilder().name(getName("screenshotBlur"))
                                            .options(List.of(enableScreenshotBlurOpt, screenshotBlurQualityOpt, screenshotBlurStrengthOpt)).build())
                                    .group(OptionGroup.createBuilder().name(getName("tint"))
                                            .options(List.of(tintColorOpt, tintStrengthOpt)).build())
                                    .build())
                            .category(ConfigCategory.createBuilder()
                                    .name(getName("capturing"))
                                    .tooltip(getDesc("capturing"))
                                    .options(List.of(archiveScreenshotsOpt, resolutionOpt, updateWorldIconOpt))
                                    .build());
                });
    }

    @ConfigEntry public int time = 80;
    @ConfigEntry public int fade = 20;

    @ConfigEntry public Color tintColor = new Color(0x212121);
    @ConfigEntry public float tintStrength = 0.3f;

    @ConfigEntry public boolean enableScreenshotBlur = false;

    @ConfigEntry public float screenshotBlurStrength = 1f; //min = 1f, max = 16f
    @ConfigEntry public float screenshotBlurQuality = 5f; //min = 1f, max = 16f

    @ConfigEntry public boolean playSoundEffect = false;

    @ConfigEntry public String soundEffect = SoundEvents.UI_TOAST_OUT.getId().toString();

    @ConfigEntry public float soundPitch = 1f; //min = 0f, max = 10f
    @ConfigEntry public float soundVolume = 1f; //min = 0f, max = 10f

    @ConfigEntry public ScreenshotResolution resolution = ScreenshotResolution.Normal;
    @ConfigEntry public boolean disableCamera = true;
    @ConfigEntry public boolean archiveScreenshots = false;
    @ConfigEntry public boolean updateWorldIcon = false;

    public enum ScreenshotResolution {
        Native(0,0),
        Normal(4000,1600),
        r4K(4000, 2160),
        r8K(7900,4320);

        public int width, height;
        ScreenshotResolution(int width_in, int height_in) {
            width = width_in;
            height = height_in;
        }
    }
}
