package org.bali.balisdelight.client.recipebook;

import com.google.common.base.Supplier;
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

public class RecipeCategories {
    public static final Supplier<RecipeBookCategories> COOKING_SEARCH = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_SEARCH", new ItemStack(Items.COMPASS)));
    public static final Supplier<RecipeBookCategories> COOKING_MEALS = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_MEALS", new ItemStack(ModItem.CRISPY_CREAM_MUSHROOM_SOUP.get())));
    public static final Supplier<RecipeBookCategories> COOKING_DRINKS = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_DRINKS", new ItemStack(Items.MILK_BUCKET)));
    public static final Supplier<RecipeBookCategories> COOKING_MISC = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_MISC", new ItemStack(ModItem.STONE_POT.get()), new ItemStack(ModItem.OVEN.get())));
    public static final Supplier<RecipeBookCategories> COOKING_COFFEE_PRODUCT = Suppliers.memoize(()->RecipeBookCategories.create("COOKING_COFFEE_PRODUCT",new ItemStack(ModItem.COFFEE_BEANS.get())));

    public static void init(RegisterRecipeBookCategoriesEvent event) {
        System.out.println("=== 配方分类注册 ===");

        event.registerBookCategories(BalisDelight.RECIPE_BOOK_TYPE, ImmutableList.of(COOKING_SEARCH.get(), COOKING_MEALS.get(), COOKING_DRINKS.get(), COOKING_MISC.get(), COOKING_COFFEE_PRODUCT.get()));
        event.registerAggregateCategory(COOKING_SEARCH.get(), ImmutableList.of(COOKING_MEALS.get(), COOKING_DRINKS.get(), COOKING_MISC.get(), COOKING_COFFEE_PRODUCT.get()));
        event.registerRecipeCategoryFinder(ModRecipeTypes.COOKING_BY_OVEN.get(), recipe ->
        {
            System.out.println("=== 配方分类查找器被调用 ===");
            System.out.println("配方ID: " + recipe.getId());
            System.out.println("配方类型: " + recipe.getType());
            if (recipe instanceof OvenBlockRecipe ovenCookingRecipe) {
                OvenBlockRecipeBookTab tab = ovenCookingRecipe.getRecipeBookTab();
                System.out.println("配方书标签: " + tab);
                System.out.println("标签序列化名称: " + (tab != null ? tab.getSerializedName() : "null"));
                if (tab != null) {
                    return switch (tab) {
                        case MEALS -> {
                            System.out.println("分配到 MEALS 分类");
                            yield COOKING_MEALS.get();
                        }

                        case COFFEE_PRODUCT -> {
                            System.out.println("分配到 COFFEE_PRODUCT 分类");
                            yield COOKING_COFFEE_PRODUCT.get();
                        }
                    };
                }
            }
            System.out.println("使用默认分类: COOKING_MISC");
            return COOKING_MISC.get();
        });
        System.out.println("配方分类注册完成");
    }
}
