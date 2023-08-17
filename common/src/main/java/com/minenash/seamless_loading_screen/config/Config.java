package com.minenash.seamless_loading_screen.config;

public class Config extends MidnightConfig {

    @Entry(min = 0)
    public static int time = 80;

    @Entry(min = 0)
    public static int fade = 20;

    @Entry(isColor = true)
    public static String tintColor = "#121212";

    @Entry(min = 0f, max = 1f, isSlider = true)
    public static float tintStrength = 0.3f;

    @Entry public static ScreenshotResolution resolution = ScreenshotResolution.Normal;
    @Entry public static boolean disableCamera = true;
    @Entry public static boolean archiveScreenshots = false;
    @Entry public static boolean updateWorldIcon = false;

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
