package org.bali.balisdelight.client.recipebook;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.common.crafting.OvenBlockRecipe;
import org.bali.balisdelight.common.registry.ModItem;
import org.bali.balisdelight.common.registry.ModRecipeTypes;

import java.util.function.Supplier;

public class RecipeCategories {
    public static final Supplier<RecipeBookCategories> OVEN_COOKING_SEARCH = Suppliers.memoize(() -> RecipeBookCategories.create("OVEN_COOKING_SEARCH", new ItemStack(Items.COMPASS)));
    public static final Supplier<RecipeBookCategories> OVEN_COOKING_MEALS = Suppliers.memoize(() -> RecipeBookCategories.create("OVEN_COOKING_MEALS", new ItemStack(ModItem.CRISPY_CREAM_MUSHROOM_SOUP.get())));
    public static final Supplier<RecipeBookCategories> OVEN_COOKING_DRINKS = Suppliers.memoize(() -> RecipeBookCategories.create("OVEN_COOKING_DRINKS", new ItemStack(Items.MILK_BUCKET)));
    public static final Supplier<RecipeBookCategories> OVEN_COOKING_MISC = Suppliers.memoize(() -> RecipeBookCategories.create("OVEN_COOKING_MISC", new ItemStack(ModItem.STONE_POT.get()), new ItemStack(ModItem.OVEN.get())));
    public static final Supplier<RecipeBookCategories> OVEN_COOKING_COFFEE_PRODUCT = Suppliers.memoize(()->RecipeBookCategories.create("OVEN_COOKING_COFFEE_PRODUCT",new ItemStack(ModItem.COFFEE_BEANS.get())));

    public static void init(RegisterRecipeBookCategoriesEvent event) {
        System.out.println("=== 配方分类注册 ===");

        event.registerBookCategories(BalisDelight.RECIPE_BOOK_TYPE, ImmutableList.of(OVEN_COOKING_SEARCH.get(), OVEN_COOKING_MEALS.get(), OVEN_COOKING_DRINKS.get(), OVEN_COOKING_MISC.get(), OVEN_COOKING_COFFEE_PRODUCT.get()));
        event.registerAggregateCategory(OVEN_COOKING_SEARCH.get(), ImmutableList.of(OVEN_COOKING_MEALS.get(), OVEN_COOKING_DRINKS.get(), OVEN_COOKING_MISC.get(), OVEN_COOKING_COFFEE_PRODUCT.get()));
        event.registerRecipeCategoryFinder(ModRecipeTypes.COOKING_BY_OVEN.get(), recipe ->
        {
            if (recipe instanceof OvenBlockRecipe ovenBlockRecipe) {
                OvenBlockRecipeBookTab tab = ovenBlockRecipe.getRecipeBookTab();
                if (tab != null) {
                    return switch (tab) {

                        case MEALS -> OVEN_COOKING_MEALS.get();

                        case DRINK ->  OVEN_COOKING_DRINKS.get();

                        case COFFEE_PRODUCT -> OVEN_COOKING_COFFEE_PRODUCT.get();

                        case MISC -> OVEN_COOKING_MISC.get();
                    };
                }
            }
            return OVEN_COOKING_MISC.get();
        });
    }
}
