package com.minenash.seamless_loading_screen;

public enum DisplayMode {
    enabled, freeze, disable;

    public DisplayMode next() {
        return DisplayMode.values()[(ordinal() + 1) % 3];
    }

}
