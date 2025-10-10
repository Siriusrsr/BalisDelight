package org.bali.balisdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.common.block.entity.container.OvenBlockMenu;
import org.bali.balisdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class OvenBlockScreen extends AbstractContainerScreen<OvenBlockMenu> implements RecipeUpdateListener {
    @SuppressWarnings("all")
    private static final ResourceLocation Recipe_Button_Location = new ResourceLocation("textures/gui/recipe_button.png");
    @SuppressWarnings("all")
    private static final ResourceLocation BackGround_Texture = new ResourceLocation(BalisDelight.MOD_ID, "textures/gui/oven.png");
//    private static final Rectangle Heat_Icon = new Rectangle(47, 55, 17, 15);
    public static final  Rectangle Progress_Arrow = new Rectangle(89, 25, 0, 17);

    private final OvenBlockRecipeBookComponent recipeBookComponent = new OvenBlockRecipeBookComponent();
    private boolean widthTooNarrow;

    public OvenBlockScreen(OvenBlockMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void init(){
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width,this.height,this.minecraft,this.widthTooNarrow,this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width,this.imageWidth);

        this.addRenderableWidget(new ImageButton(this.leftPos + 5,this.height / 2 - 49,20,18,0,0,19,Recipe_Button_Location,(button)->{
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width,this.imageWidth);
            ((ImageButton) button).setPosition(this.leftPos+5,this.height / 2 - 49);
        }));
        this.addWidget(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
    }

    @Override
    protected void containerTick(){
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    public void render(GuiGraphics gui,final int mouseX,final int mouseY,float partialTicks){
        this.renderBackground(gui);

        if (this.recipeBookComponent.isVisible()&& this.widthTooNarrow){
            this.renderBg(gui,partialTicks,mouseX,mouseY);
            this.recipeBookComponent.render(gui,mouseX,mouseY,partialTicks);
        } else {
            this.recipeBookComponent.render(gui,mouseX,mouseY,partialTicks);
            super.render(gui,mouseX,mouseY,partialTicks);
            this.recipeBookComponent.renderGhostRecipe(gui,this.leftPos,this.topPos,false,partialTicks);
        }

        this.renderMealDisplayTooltip(gui,mouseX,mouseY);
        this.recipeBookComponent.renderTooltip(gui,this.leftPos,this.topPos,mouseX,mouseY);
    }

    protected void renderMealDisplayTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if (this.minecraft != null && this.minecraft.player != null && this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            if (this.hoveredSlot.index == 6) {
                List<Component> tooltip = new ArrayList<>();

                ItemStack mealStack = this.hoveredSlot.getItem();
                tooltip.add(((MutableComponent) mealStack.getItem().getDescription()).withStyle(mealStack.getRarity().color));

                ItemStack containerStack = this.menu.blockEntity.getContainer();
                String container = !containerStack.isEmpty() ? containerStack.getItem().getDescription().getString() : "";

                tooltip.add(TextUtils.getTranslation("container.oven_block.served_on", container).withStyle(ChatFormatting.GRAY));

                gui.renderComponentTooltip(font, tooltip, mouseX, mouseY);
            } else {
                gui.renderTooltip(font, this.hoveredSlot.getItem(), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
//        背景
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft == null)
            return;

        gui.blit(BackGround_Texture,this.leftPos,this.topPos,0,0,this.imageWidth,this.imageHeight);

//        进度箭头
        int l = this.menu.getCookProgressionScaled();
        gui.blit(BackGround_Texture,this.leftPos+Progress_Arrow.x,this.topPos+Progress_Arrow.y,176,15,l+1,Progress_Arrow.height);
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, buttonId)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        }
        return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(mouseX, mouseY, buttonId);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int x, int y, int buttonIdx) {
        boolean flag = mouseX < (double) x || mouseY < (double) y || mouseX >= (double) (x + this.imageWidth) || mouseY >= (double) (y + this.imageHeight);
        return flag && this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, buttonIdx);
    }

    @Override
    protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType clickType) {
        super.slotClicked(slot, mouseX, mouseY, clickType);
        this.recipeBookComponent.slotClicked(slot);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    @Nonnull
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
