package com.minenash.seamless_loading_screen;

public enum DisplayMode {
    ENABLED, FREEZE, DISABLED;

    public DisplayMode next() {
        return DisplayMode.values()[(ordinal() + 1) % 3];
    }

}
