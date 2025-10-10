package org.bali.balisdelight.common.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.common.crafting.OvenBlockRecipe;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BalisDelight.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> COOKING_BY_OVEN = RECIPE_SERIALIZERS.register("cookingbyoven",OvenBlockRecipe.Serializer::new);

}
