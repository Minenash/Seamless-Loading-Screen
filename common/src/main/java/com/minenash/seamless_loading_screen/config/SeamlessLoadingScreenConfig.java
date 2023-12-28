package com.minenash.seamless_loading_screen.config;

import com.minenash.seamless_loading_screen.DisplayMode;
import com.minenash.seamless_loading_screen.PlatformFunctions;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.List;

public class SeamlessLoadingScreenConfig {
    private static final SafeColorTypeAdapter colorAdapter = new SafeColorTypeAdapter(() -> getDefaults().tintColor);
    private static final ConfigClassHandler<SeamlessLoadingScreenConfig> CONFIG_CLASS_HANDLER = ConfigClassHandler
            .createBuilder(SeamlessLoadingScreenConfig.class)
            .id(new Identifier("seamless_loading_screen", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .appendGsonBuilder(builder -> builder.setPrettyPrinting()
                            .disableHtmlEscaping()
                            .serializeNulls()
                            .registerTypeHierarchyAdapter(Color.class, colorAdapter))
                    .setPath(PlatformFunctions.getConfigDirectory().resolve("seamless_loading_screen.json"))
                    .build()
            )
            .build();

    //=====================
    @SerialEntry
    public int fade = 20;
    @SerialEntry
    public int time = 80;
    //=====================
    @SerialEntry
    public Color tintColor = new Color(0x212121);
    @SerialEntry
    public float tintStrength = 0.3f;
    @SerialEntry
    public boolean enableScreenshotBlur = false;
    @SerialEntry
    public float screenshotBlurStrength = 1f; //min = 1f, max = 16f
    @SerialEntry
    public float screenshotBlurQuality = 5f; //min = 1f, max = 16f
    @SerialEntry
    public boolean playSoundEffect = false;
    @SerialEntry
    public String soundEffect = SoundEvents.UI_TOAST_OUT.getId().toString();
    @SerialEntry
    public float soundPitch = 1f; //min = 0f, max = 10f
    @SerialEntry
    public float soundVolume = 1f; //min = 0f, max = 10f
    @SerialEntry
    public ScreenshotResolution resolution = ScreenshotResolution.Normal;
    @SerialEntry
    public boolean disableCamera = true;
    @SerialEntry
    public boolean archiveScreenshots = false;
    @SerialEntry
    public boolean updateWorldIcon = false;
    @SerialEntry
    public List<String> blacklistedAddresses = List.of("play.wynncraft.com");
    @SerialEntry
    public boolean saveScreenshotsByUsername = false;
    @SerialEntry
    public DisplayMode defaultServerMode = DisplayMode.DISABLED;
    //=====================

    private static SeamlessLoadingScreenConfig getDefaults() {
        return CONFIG_CLASS_HANDLER.defaults();
    }

    public static SeamlessLoadingScreenConfig get() {
        return CONFIG_CLASS_HANDLER.instance();
    }

    //=====================

    public static void load() {
        CONFIG_CLASS_HANDLER.load();

        //Check if color is broken within config to save over such i.e tintColor
        if (colorAdapter.errored()) CONFIG_CLASS_HANDLER.save();
    }

    public static void save() {
        CONFIG_CLASS_HANDLER.save();
    }

    private static Text getName(String id) {
        return Text.translatable("seamless_loading_screen.config." + id);
    }

    private static Text getDesc(String id) {
        return Text.translatable("seamless_loading_screen.config." + id + ".description");
    }
    private static Identifier getImg(String id) {
        return new Identifier("seamless_loading_screen", "textures/config/" + id + ".webp");
    }

    public static YetAnotherConfigLib getInstance() {
        return YetAnotherConfigLib.create(CONFIG_CLASS_HANDLER,
                (defaults, config, builder) -> {

                    var timeOpt = Option.<Integer>createBuilder()
                            .name(getName("time"))
                            .description(OptionDescription.createBuilder()
                                    .text(getDesc("time"))
//                                    .webpImage(getImg(""))
                                    .build())
                            .binding(defaults.time, () -> config.time, (val) -> config.time = val)
                            .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(0))
                            .build();

                    var fadeOpt = Option.<Integer>createBuilder()
                            .name(getName("fade"))
                            .description(OptionDescription.createBuilder()
                                    .text(getDesc("fade"))
                                    .build())
                            .binding(defaults.fade, () -> config.fade, (val) -> config.fade = val)
                            .controller(opt -> IntegerFieldControllerBuilder.create(opt).min(0))
                            .build();

                    var defaultServerModeOpt = Option.<com.minenash.seamless_loading_screen.DisplayMode>createBuilder()
                            .name(getName("serverDisplayMode"))
                            .description(OptionDescription.createBuilder().text(getDesc("serverDisplayMode")).build())
                            .binding(defaults.defaultServerMode, () -> config.defaultServerMode, (val) -> config.defaultServerMode = val)
                            .controller(opt -> EnumControllerBuilder.create(opt)
                                    .enumClass(DisplayMode.class)
                                    .valueFormatter(val -> Text.translatable("seamless_loading_screen.config.displayMode." + val.name().toLowerCase()))
                            ).build();

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

                    var blacklistedAddressOpt = ListOption.<String>createBuilder()
                            .name(getName("blacklistedAddresses"))
                            .description(OptionDescription.createBuilder().text(getDesc("blacklistedAddresses")).build())
                            .binding(defaults.blacklistedAddresses, () -> config.blacklistedAddresses, val -> config.blacklistedAddresses = val)
                            .controller(StringControllerBuilder::create)
                            .initial("")
                            .build();

                    var saveScreenshotsByUsernameOpt = Option.<Boolean>createBuilder()
                            .name(getName("saveScreenshotsByUser"))
                            .description(OptionDescription.createBuilder().text(getDesc("saveScreenshotsByUser")).build())
                            .binding(defaults.saveScreenshotsByUsername, () -> config.saveScreenshotsByUsername, (val) -> config.saveScreenshotsByUsername = val)
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
                                    .options(List.of(saveScreenshotsByUsernameOpt, archiveScreenshotsOpt, resolutionOpt, updateWorldIconOpt))
                                    .build())
                            .category(ConfigCategory.createBuilder()
                                    .name(getName("server_settings"))
                                    .tooltip(getDesc("server_settings"))
                                    .options(List.of(defaultServerModeOpt))
                                    .group(blacklistedAddressOpt)
                                    .build());
                });
    }

    public enum ScreenshotResolution {
        Native(0, 0),
        Normal(4000, 1600),
        r4K(4000, 2160),
        r8K(7900, 4320);

        public int width, height;

        ScreenshotResolution(int width_in, int height_in) {
            width = width_in;
            height = height_in;
        }
    }


}
