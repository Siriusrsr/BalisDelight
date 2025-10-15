package org.bali.balisdelight.common.registry;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.common.crafting.OvenBlockRecipe;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, BalisDelight.MOD_ID);

    public static final RegistryObject<RecipeType<OvenBlockRecipe>> COOKING_BY_OVEN = RECIPE_TYPES.register("cooking_by_oven", () -> registerRecipeType("cooking_by_oven"));

    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String identifier) {
        return new RecipeType<>()
        {
            public String toString() {
                return BalisDelight.MOD_ID + ":" + identifier;
            }
        };
    }
}
