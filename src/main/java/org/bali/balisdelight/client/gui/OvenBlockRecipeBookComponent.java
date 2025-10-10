package org.bali.balisdelight.client.gui;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;

public class OvenBlockRecipeBookComponent extends RecipeBookComponent {

    @SuppressWarnings("all")
    protected static final ResourceLocation Recipe_Book_Buttons = new ResourceLocation(BalisDelight.MOD_ID,"textures/gui/recipe_book_buttons.png");

    @Override
    protected void initFilterButtonTextures(){
        this.filterButton.initTextureValues(0,0,28,18,Recipe_Book_Buttons);
    }

    public void hide(){
        this.setVisible(false);
    }

    @Override
    @Nonnull
    protected Component getRecipeFilterName(){
        return TextUtils.getTranslation("container.recipe_book.cookable");
    }
}
