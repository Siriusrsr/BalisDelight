package org.bali.balisdelight.common.block.entity.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraft.core.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CookingOvenItemHandler implements IItemHandler {
    private static final int slots_input = 6;
    private static final int slot_container_input = 7;
    private static final int slot_meal_output = 8;
    private final IItemHandler itemHandler;
    private final Direction side;

    public CookingOvenItemHandler(IItemHandler itemHandler, @Nullable Direction side) {
        this.itemHandler = itemHandler;
        this.side = side;
    }

    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return itemHandler.isItemValid(slot, stack);
    }

    @Override
    public int getSlots(){
        return itemHandler.getSlots();
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot){
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot,@Nonnull ItemStack stack,boolean simulate){
        if (side == null || side.equals(Direction.NORTH)){
            return slot < slots_input ? itemHandler.insertItem(slot, stack, simulate) : stack;
        }
        else {
            return slot == slot_container_input ? itemHandler.insertItem(slot, stack, simulate) : stack;
        }
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate){
        if (side == null || side.equals(Direction.SOUTH)){
            return slot < slots_input ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
        else {
            return slot == slot_meal_output ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlotLimit(int slot){
        return itemHandler.getSlotLimit(slot);
    }
}
