package com.minenash.seamless_loading_screen.config;

public class Config extends TinyConfig {

    @Entry(min = 0)
    public static int time = 80;

    @Entry(min = 0)
    public static int fade = 20;

    @Entry public static boolean disableCamera = true;
    @Entry public static boolean disableHRImage = false;
}
