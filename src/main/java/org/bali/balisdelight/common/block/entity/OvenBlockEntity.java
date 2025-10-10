package org.bali.balisdelight.common.Block.Entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.bali.balisdelight.common.Block.Entity.Inventory.CookingOvenItemHandler;
import org.bali.balisdelight.common.registry.ModBlockEntityTypes;
import org.bali.balisdelight.common.registry.ModItem;


public class OvenBlockEntity extends SyncedBlockEntity{
    private static final int inventory_slot_count = 6;
    public static final int meal_display_slot=6;
    public static final int container_slot=7;
    public static final int output_slot=8;
    public static final int inventory_size =output_slot+1;

    private final ItemStackHandler  inventory;
    private final LazyOptional<IItemHandler>  inputHandler;
    private final LazyOptional<IItemHandler>  outputHandler;

    private int cookTime;
    private int cookTimeTotal;
    private ItemStack mealContainerStack;
    private Component customName;
    private boolean checkNewRecipe;

    protected final ContainerData cookingOvenData;


    public OvenBlockEntity(BlockPos pos, BlockState state){
        super(ModBlockEntityTypes.oven.get(),pos,state);
        inventory = createHandler();
        this.inputHandler = LazyOptional.of(()-> new CookingOvenItemHandler(inventory, Direction.UP));
        this.outputHandler = LazyOptional.of(()->new CookingOvenItemHandler(inventory,Direction.DOWN));
        this.mealContainerStack = ItemStack.EMPTY;
        this.cookingOvenData = createInArray();
        this. checkNewRecipe = true;

    }

    private ItemStackHandler createHandler(){
        return new ItemStackHandler(inventory_size)
        {
            @Override
            public void onContentsChanged(int slot){
                if (slot >= 0&&slot < meal_display_slot){
                    checkNewRecipe = true;
                }
                inventoryChanged();
            }
        };
    }

    public static ItemStack getMealFromItem(ItemStack cookingOvenStack){
        if(!cookingOvenStack.is(ModItem.oven.get())) {
            return ItemStack.EMPTY;
        }
        CompoundTag compound = cookingOvenStack.getTagElement("BlockEntityTag");
        if(compound != null){
            CompoundTag inventoryTag = compound.getCompound("Inventory");
            if(inventoryTag.contains("Items",9)){
                ItemStackHandler handler =new ItemStackHandler();
                handler.deserializeNBT(inventoryTag);
                return handler.getStackInSlot(0);
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getContainerFromItem(ItemStack cookingOvenStack){
        if(!cookingOvenStack.is(ModItem.oven.get())) {
            return ItemStack.EMPTY;
        }

        CompoundTag compound = cookingOvenStack.getTagElement("BlockEntityTag");
        if(compound!=null){
            return ItemStack.of(compound.getCompound("Container"));
        }
        return ItemStack.EMPTY;
    }

    /**
     * 通过ContainerData端口在客户端和服务端之间传递已制作时间和总时间
     */
    private ContainerData createInArray(){
        return new ContainerData()
        {
            @Override
            public int get(int index){
                return switch (index){
                    case 0 -> OvenBlockEntity.this.cookTime;
                    case 1 -> OvenBlockEntity.this.cookTimeTotal;
                    default -> 0;
                };
            }
            @Override
            public void set(int index, int value){
                switch (index){
                    case 0 -> OvenBlockEntity.this.cookTime = value;
                    case 1 -> OvenBlockEntity.this.cookTimeTotal = value;
                }
            }
            @Override
            public int getCount() {
                return 2;
            }
        };
    }
}
