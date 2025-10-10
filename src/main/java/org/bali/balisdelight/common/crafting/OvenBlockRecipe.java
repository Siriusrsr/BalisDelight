package org.bali.balisdelight.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraft.core.NonNullList;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.client.recipebook.OvenBlockRecipeBookTab;
import org.bali.balisdelight.common.registry.ModItem;
import org.bali.balisdelight.common.registry.ModRecipeSerializers;
import org.bali.balisdelight.common.registry.ModRecipeTypes;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class OvenBlockRecipe implements Recipe<RecipeWrapper> {
    public static final int INPUT_SLOTS = 6;

    private final ResourceLocation id;
    private final String group;
    private final OvenBlockRecipeBookTab tab;
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ItemStack container;
    private final float experience;
    private final int cookTime;

    public OvenBlockRecipe(ResourceLocation id, String group, @Nullable OvenBlockRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
        this.id = id;
        this.group = group;
        this.tab = tab;
        this.inputItems = inputItems;

        this.output = output;

    if (!container.isEmpty()) {
        this.container = container;
    } else if (!output.getCraftingRemainingItem().isEmpty()) {
        this.container = output.getCraftingRemainingItem();
    } else {
        this.container = ItemStack.EMPTY;
    }

        this.experience = experience;
        this.cookTime = cookTime;
    }

    @Nullable
    public OvenBlockRecipeBookTab getRecipeBookTab() {
        return this.tab;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public ItemStack getOutputContainer() {
        return this.container;
    }


        @Override
    public ItemStack assemble(RecipeWrapper inv, RegistryAccess access) {
        return this.output.copy();
    }

    public float getExperience() {
        return this.experience;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for (int j = 0; j < INPUT_SLOTS; ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }
        return i == this.inputItems.size() && net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.inputItems) != null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.inputItems.size();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.COOKING_BY_OVEN.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.COOKING_BY_OVEN.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModItem.OVEN.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OvenBlockRecipe that = (OvenBlockRecipe) o;

        if (Float.compare(that.getExperience(), getExperience()) != 0) return false;
        if (getCookTime() != that.getCookTime()) return false;
        if (!getId().equals(that.getId())) return false;
        if (!getGroup().equals(that.getGroup())) return false;
        if (tab != that.tab) return false;
        if (!inputItems.equals(that.inputItems)) return false;
        if (!output.equals(that.output)) return false;
        return container.equals(that.container);
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getGroup().hashCode();
        result = 31 * result + (getRecipeBookTab() != null ? getRecipeBookTab().hashCode() : 0);
        result = 31 * result + inputItems.hashCode();
        result = 31 * result + output.hashCode();
        result = 31 * result + container.hashCode();
        result = 31 * result + (getExperience() != +0.0f ? Float.floatToIntBits(getExperience()) : 0);
        result = 31 * result + getCookTime();
        return result;
    }

    public static class Serializer implements RecipeSerializer<OvenBlockRecipe> {
        public Serializer() {
        }

        @Override
        public OvenBlockRecipe fromJson(ResourceLocation recipeId, JsonObject json){
            final String groupIn = GsonHelper.getAsString(json, "group", "");
            final NonNullList<Ingredient> inputItemsIn = readIngredients(GsonHelper.getAsJsonArray(json,"ingredients"));
            if (inputItemsIn.isEmpty()) {
                throw new JsonParseException("No ingredients for oven cooking recipe ");
            } else if (inputItemsIn.size()>OvenBlockRecipe.INPUT_SLOTS) {
                throw new JsonParseException("Too many ingredients for cooking recipe! The max is " + OvenBlockRecipe.INPUT_SLOTS);
            } else {
                final String tabKeyIn = GsonHelper.getAsString(json,"recipe_book_tab",null);
                final OvenBlockRecipeBookTab tabIn = OvenBlockRecipeBookTab.findByName(tabKeyIn);
                if (tabKeyIn != null && tabIn == null) {
                    BalisDelight.LOGGER.warn("Optional field 'recipe_book_tab' does not match any valid tab. If defined, must be one of the following: " + EnumSet.allOf(OvenBlockRecipeBookTab.class));
                }
                final ItemStack outputIn = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json,"result"),true);
                ItemStack container = GsonHelper.isValidNode(json,"container")?CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json,"container"),true):ItemStack.EMPTY;
                final float experienceIn = GsonHelper.getAsFloat(json,"experience",0.0F);
                final int cookTimeIn = GsonHelper.getAsInt(json,"cookTime",200);
                return new OvenBlockRecipe(recipeId,groupIn,tabIn,inputItemsIn,outputIn,container,experienceIn,cookTimeIn);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray){
            NonNullList<Ingredient> nonNullList = NonNullList.create();

            for(int i = 0; i < ingredientArray.size();++i){
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonNullList.add(ingredient);
                }
            }
            return nonNullList;
        }

        @Nullable
        @Override
        public OvenBlockRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String groupIn = buffer.readUtf();
            OvenBlockRecipeBookTab tabIn = OvenBlockRecipeBookTab.findByName(buffer.readUtf());
            int i = buffer.readVarInt();
            NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);

            for (int j = 0; j < inputItemsIn.size(); ++j) {
                inputItemsIn.set(j, Ingredient.fromNetwork(buffer));
            }

            ItemStack outputIn = buffer.readItem();
            ItemStack container = buffer.readItem();
            float experienceIn = buffer.readFloat();
            int cookTimeIn = buffer.readVarInt();
            return new OvenBlockRecipe(recipeId, groupIn, tabIn, inputItemsIn, outputIn, container, experienceIn, cookTimeIn);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, OvenBlockRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
            buffer.writeVarInt(recipe.inputItems.size());

            for (Ingredient ingredient : recipe.inputItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.output);
            buffer.writeItem(recipe.container);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }
    }

}
