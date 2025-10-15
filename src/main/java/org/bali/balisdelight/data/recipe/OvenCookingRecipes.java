package org.bali.balisdelight.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import org.bali.balisdelight.client.recipebook.OvenBlockRecipeBookTab;
import org.bali.balisdelight.common.registry.ModItem;
import org.bali.balisdelight.common.tag.ForgeTags;
import org.bali.balisdelight.common.tag.ModTags;
import org.bali.balisdelight.data.builder.OvenBlockRecipeBuilder;

import java.util.function.Consumer;

public class OvenCookingRecipes {
    public static final int FAST_COOKING = 100;      // 5 seconds
    public static final int NORMAL_COOKING = 200;    // 10 seconds
    public static final int SLOW_COOKING = 400;      // 20 seconds

    public static final float SMALL_EXP = 0.35F;
    public static final float MEDIUM_EXP = 1.0F;
    public static final float LARGE_EXP = 2.0F;

    public static void register(Consumer<FinishedRecipe> consumer) {
        cookMiscellaneous(consumer);
    }

    public static void cookMiscellaneous(Consumer<FinishedRecipe> consumer) {
        OvenBlockRecipeBuilder.cookingPotRecipe(ModItem.CRISPY_CREAM_MUSHROOM_SOUP.get(),1,NORMAL_COOKING,MEDIUM_EXP)
                .addIngredient(ForgeTags.MILK)
                .addIngredient(Items.WHEAT)
                .addIngredient(ForgeTags.MILK)
                .addIngredient(Items.BROWN_MUSHROOM)
                .addIngredient(Items.CAKE)
                .addIngredient(Items.BROWN_MUSHROOM)
                .setRecipeBookTab(OvenBlockRecipeBookTab.MEALS)
                .unlockedByAnyIngredient(Items.MILK_BUCKET,Items.BROWN_MUSHROOM,ModItem.STONE_POT.get())
                .build(consumer);
        OvenBlockRecipeBuilder.cookingPotRecipe(ModItem.CRISPY_CREAM_MUSHROOM_SOUP.get(),1,SLOW_COOKING,MEDIUM_EXP)
                .addIngredient(Items.MILK_BUCKET)
                .addIngredient(Items.WHEAT)
                .setRecipeBookTab(OvenBlockRecipeBookTab.MEALS)
                .build(consumer);
    }

}
