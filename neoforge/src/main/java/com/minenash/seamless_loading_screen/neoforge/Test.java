package com.minenash.seamless_loading_screen.neoforge;

import dev.isxander.yacl3.gui.ElementListWidgetExt;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.Widget;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Test {}/*<E extends ElementListWidgetExt.Entry<E>> extends ElementListWidget<E> implements Widget {

    @Nullable
    @Override
    protected E getEntryAtPosition(double x, double y) {
        y += getScrollAmount();

        if (x < this.getX() || x > this.getX() + this.getWidth())
            return null;

        int currentY = this.getY() - headerHeight + 4;
        for (E entry : children()) {
            if (y >= currentY && y <= currentY + entry.getItemHeight()) {
                return entry;
            }

            currentY += entry.getItemHeight();
        }

        return null;
    }

    public List<E> children(){
        return List.of();
    }

}*/
