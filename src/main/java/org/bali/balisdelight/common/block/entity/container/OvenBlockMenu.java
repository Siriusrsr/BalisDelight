package org.bali.balisdelight.common.block.entity.container;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.common.block.entity.OvenBlockEntity;
import org.bali.balisdelight.common.registry.ModBlock;
import org.bali.balisdelight.common.registry.ModMenuTypes;
import org.bali.balisdelight.common.tag.ModTags;

import java.util.Objects;

public class OvenBlockMenu extends RecipeBookMenu<RecipeWrapper> {
    @SuppressWarnings("all")
    public static final ResourceLocation Empty_Container_Slot_Bowl = new ResourceLocation(BalisDelight.MOD_ID, "item/empty_container_slot_bowl");

    public final OvenBlockEntity blockEntity;
    public final ItemStackHandler inventory;
    private final ContainerData ovenBlockData;
    private final ContainerLevelAccess canInteractWithCallable;
    protected final Level level;

    public OvenBlockMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        this(windowId,playerInventory,getTileEntity(playerInventory,data),new SimpleContainerData(4));
    }

    public OvenBlockMenu(final int windowId, final Inventory playerInventory, final OvenBlockEntity blockEntity, ContainerData ovenBlockDataIn) {
        super(ModMenuTypes.OVEN_BLOCK.get(),windowId);
        this.blockEntity = blockEntity;
        this.inventory = blockEntity.getInventory();
        this.ovenBlockData = ovenBlockDataIn;
        this.level = playerInventory.player.level();
        this.canInteractWithCallable = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        //原材料槽位 两行三列
        int startX = 8;
        int startY = 18;
        int inputStartX = 30;
        int inputStartY = 17;
        int borderSlotSize = 18;
        for (int row = 0; row <2;++row){
            for (int col = 0;col <3;++col){
                this.addSlot(new SlotItemHandler(inventory,(row*3)+col,
                        inputStartX + (col*borderSlotSize),
                        inputStartY + (row*borderSlotSize)));
            }
        }

//        产出菜位置
        this.addSlot(new OvenBlockMealSlot(inventory,6,124,26));

//        容器输入
        this.addSlot(new SlotItemHandler(inventory,7,92,55){
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon(){
                return Pair.of(InventoryMenu.BLOCK_ATLAS,Empty_Container_Slot_Bowl);
            }
        });

//        容器输出
        this.addSlot(new OvenBlockResultSlot(blockEntity,playerInventory.player,inventory,8,124,55));

//        玩家物品栏
        int startPlayerInvY = startY *4+12;
        for(int row = 0;row<3;++row){
            for(int col = 0;col<9;++col){
                this.addSlot(new Slot(playerInventory,9+(row*9)+col,startX+(col*borderSlotSize),startPlayerInvY+(row*borderSlotSize)));
            }
        }

//        快捷栏
        for(int col=0;col<9;++col){
            this.addSlot(new Slot(playerInventory,col,startX+(col*borderSlotSize),142));
        }

        this.addDataSlots(ovenBlockDataIn);
    }

    private static OvenBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "bata cannot be null");
        final BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof OvenBlockEntity) {
            return (OvenBlockEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct" + tileAtPos);
    }

    @Override
    public boolean stillValid(Player playerIn){
        return stillValid(canInteractWithCallable,playerIn, ModBlock.OVEN.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        int indexMealDisplay = 6;
        int indexContainerInput = 7;
        int indexOutput = 8;
        int startPlayerInv = indexOutput + 1;
        int endPlayerInv = startPlayerInv + 36;
        ItemStack slotStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            slotStackCopy = slotStack.copy();
            if (index == indexOutput) {
                if (!this.moveItemStackTo(slotStack, startPlayerInv, endPlayerInv, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index > indexOutput){
                boolean isValidContainer = slotStack.is(ModTags.OVEN_SERVING_CONTAINERS) || slotStack.is(blockEntity.getContainer().getItem()) ;
                if (isValidContainer && !this.moveItemStackTo(slotStack, indexContainerInput, indexContainerInput+1, false)) {
                    return ItemStack.EMPTY;
                } else if (!this.moveItemStackTo(slotStack,0,indexMealDisplay, false)) {
                    return ItemStack.EMPTY;
                } else if (!this.moveItemStackTo(slotStack, indexContainerInput, indexOutput, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack,startPlayerInv,endPlayerInv,false)){
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()){
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == slotStackCopy.getCount()){
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }
        return slotStackCopy;
    }

    public int getCookProgressionScaled() {
        int i = this.ovenBlockData.get(0);
        int j = this.ovenBlockData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents helper) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            helper.accountSimpleStack(inventory.getStackInSlot(i));
        }
    }

    @Override
    public void clearCraftingContent(){
        for ( int i = 0 ; i < 6; i++){
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean recipeMatches(Recipe<?super RecipeWrapper> recipe){
        return recipe.matches(new RecipeWrapper(inventory),level);
    }

    @Override
    public int getResultSlotIndex() {
        return 7;
    }

    @Override
    public int getGridWidth(){
        return 3;
    }

    @Override
    public int getGridHeight(){
        return 2;
    }

    @Override
    public int getSize(){
        return 7;
    }

    @Override
    public RecipeBookType getRecipeBookType(){
        return BalisDelight.RECIPE_BOOK_TYPE;
    }

    @Override
    public boolean shouldMoveToInventory(int slot){
        return slot < (getGridHeight()*getGridWidth());
    }
}
