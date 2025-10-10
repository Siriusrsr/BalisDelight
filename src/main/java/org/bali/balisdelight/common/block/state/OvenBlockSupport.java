package org.bali.balisdelight.common.block.state;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum OvenBlockSupport implements StringRepresentable {
    HANDLE("handle");

    private final String supportName;

    OvenBlockSupport(String name) {
        this.supportName = name;
    }

    @Override
    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.supportName;
    }
}
