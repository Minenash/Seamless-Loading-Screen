package com.minenash.seamless_loading_screen.config;

import com.minenash.seamless_loading_screen.DisplayMode;

public class Config extends MidnightConfig {

    @Entry(min = 0)
    public static int time = 80;

    @Entry(min = 0)
    public static int fade = 20;

    @Entry(isColor = true)
    public static String tintColor = "#121212";

    @Entry(min = 0f, max = 1f, isSlider = true)
    public static float tintStrength = 0.3f;

    @Entry
    public static boolean enableScreenshotBlur = false;
    @Entry(min = 1f, max = 16f, isSlider = true)
    public static float screenshotBlurStrength = 1f;
    @Entry(min = 1f, max = 16f, isSlider = true)
    public static float screenshotBlurQuality = 5f;

    @Entry
    public static boolean playSoundEffect = false;
    @Entry
    public static String soundEffect = "minecraft:ui.toast.out";
    @Entry(min = 0f, max = 10f, isSlider = true)
    public static float soundPitch = 1f;
    @Entry(min = 0f, max = 10f, isSlider = true)
    public static float soundVolume = 1f;

    @Entry public static ScreenshotResolution resolution = ScreenshotResolution.Normal;
    @Entry public static boolean disableCamera = true;
    @Entry public static boolean archiveScreenshots = false;
    @Entry public static boolean updateWorldIcon = false;

    @Entry public static boolean saveScreenshotsByUsername = false;

    @Entry public static DisplayMode defaultServerMode = DisplayMode.DISABLED;

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
