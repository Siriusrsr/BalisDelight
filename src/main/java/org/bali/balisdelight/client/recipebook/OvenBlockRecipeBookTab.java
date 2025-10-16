package org.bali.balisdelight.client.recipebook;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;


public enum OvenBlockRecipeBookTab implements StringRepresentable {
    COFFEE_PRODUCT("coffee_product"),
    MEALS("meals"),
    DRINK("drink"),
    MISC("misc");

    public final String name;

    OvenBlockRecipeBookTab(String name) {
        this.name = name;
    }

    public static OvenBlockRecipeBookTab findByName(String name) {
        for (OvenBlockRecipeBookTab value:values()){
            if (value.name.equals(name)){
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
