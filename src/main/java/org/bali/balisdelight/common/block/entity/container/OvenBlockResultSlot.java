package org.bali.balisdelight.common.block.entity.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.bali.balisdelight.common.block.entity.OvenBlockEntity;

import javax.annotation.Nonnull;

public class OvenBlockResultSlot extends SlotItemHandler{
    public final OvenBlockEntity ovenBlockEntity;
    private final Player player;
    private int removeCount;

    public OvenBlockResultSlot(OvenBlockEntity ovenBlockEntity, Player player, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
        this.ovenBlockEntity = ovenBlockEntity;
    }

    @Override
    public boolean mayPlace(ItemStack stack){
        return false;
    }

    @Override
    @Nonnull
    public ItemStack remove(int amount){
        if(this.hasItem()){
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stakc,int amount){
        this.removeCount += amount;
        this.checkTakeAchievements(stakc);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack){
        stack.onCraftedBy(this.player.level(), this.player, this.removeCount);

        if (!this.player.level().isClientSide) {
            ovenBlockEntity.awardUsedRecipes(this.player, ovenBlockEntity.getDroppableInventory());
        }

        this.removeCount = 0;
    }
}
