package com.minenash.seamless_loading_screen.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.mojang.logging.LogUtils;
import dev.isxander.yacl3.config.GsonConfigInstance;
import org.slf4j.Logger;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.function.Supplier;

public class SafeColorTypeAdapter extends GsonConfigInstance.ColorTypeAdapter {

    private static final Logger LOGGER = LogUtils.getLogger();

    public final Supplier<Color> supplier;

    public boolean errored = false;

    public SafeColorTypeAdapter(Supplier<Color> supplier){
        this.supplier = supplier;
    }

    public boolean errored(){
        boolean errored = this.errored;

        this.errored = false;

        return errored;
    }

    @Override
    public Color deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return super.deserialize(jsonElement, type, jsonDeserializationContext);
        } catch (UnsupportedOperationException e){
            LOGGER.error("Unable to parse a given color form the config file, such will be set to default! [Value: {}]", jsonElement);

            errored = true;

            return supplier.get();
        }
    }

    @Override
    public JsonElement serialize(Color color, Type type, JsonSerializationContext jsonSerializationContext) {
        return super.serialize(color, type, jsonSerializationContext);
    }
}
