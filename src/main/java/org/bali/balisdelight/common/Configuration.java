package org.bali.balisdelight.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static final String CATEGORY_RECIPE_BOOK = "recipe_book";
    public static ForgeConfigSpec.BooleanValue ENABLE_RECIPE_BOOK_OVEN;

    public static final String CATEGORY_CLIENT = "client";

    static{
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("Recipe book").push(CATEGORY_RECIPE_BOOK);
        ENABLE_RECIPE_BOOK_OVEN = COMMON_BUILDER.comment("Should the Oven have a Recipe Book available on its interface?")
                .define("enableRecipeBookOven", true);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();

        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("Client settings").push(CATEGORY_CLIENT);

        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }
}
