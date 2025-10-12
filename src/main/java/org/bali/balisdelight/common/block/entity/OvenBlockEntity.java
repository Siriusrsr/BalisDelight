package org.bali.balisdelight.common.block.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.bali.balisdelight.common.block.OvenBlock;
import org.bali.balisdelight.common.block.entity.container.OvenBlockMenu;
import org.bali.balisdelight.common.block.entity.inventory.CookingOvenItemHandler;
import org.bali.balisdelight.common.crafting.OvenBlockRecipe;
import org.bali.balisdelight.common.mixin.accessor.RecipeManagerAccessor;
import org.bali.balisdelight.common.registry.ModBlockEntityTypes;
import org.bali.balisdelight.common.registry.ModItem;
import org.bali.balisdelight.common.registry.ModRecipeTypes;
import org.bali.balisdelight.common.utility.ItemUtils;
import org.bali.balisdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;


public class OvenBlockEntity extends SyncedBlockEntity implements MenuProvider, Nameable, RecipeHolder {
    public static final int meal_display_slot=6;
    public static final int container_slot=7;
    public static final int output_slot=8;
    public static final int inventory_size =output_slot+1;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty ANIMATE = BooleanProperty.create("animate");



    public static final Map<Item, Item> INGREDIENT_REMAINDER_OVERRIDES = Map.ofEntries(
            entry(Items.POWDER_SNOW_BUCKET, Items.BUCKET),
            entry(Items.AXOLOTL_BUCKET, Items.BUCKET),
            entry(Items.COD_BUCKET, Items.BUCKET),
            entry(Items.PUFFERFISH_BUCKET, Items.BUCKET),
            entry(Items.SALMON_BUCKET, Items.BUCKET),
            entry(Items.TROPICAL_FISH_BUCKET, Items.BUCKET),
            entry(Items.SUSPICIOUS_STEW, Items.BOWL),
            entry(Items.MUSHROOM_STEW, Items.BOWL),
            entry(Items.RABBIT_STEW, Items.BOWL),
            entry(Items.BEETROOT_SOUP, Items.BOWL),
            entry(Items.POTION, Items.GLASS_BOTTLE),
            entry(Items.SPLASH_POTION, Items.GLASS_BOTTLE),
            entry(Items.LINGERING_POTION, Items.GLASS_BOTTLE),
            entry(Items.EXPERIENCE_BOTTLE, Items.GLASS_BOTTLE)
    );

    private final ItemStackHandler  inventory;
    private final LazyOptional<IItemHandler>  inputHandler;
    private final LazyOptional<IItemHandler>  outputHandler;

    private int animationTick = 0;
    private static final int ANIMATION_INTERVAL = 30; // 每30 tick切换一次（1.5秒）

    private int cookTime;
    private int cookTimeTotal;
    private ItemStack mealContainerStack;
    private Component customName;

    protected final ContainerData cookingOvenData;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    private ResourceLocation lastRecipeID;
    private boolean checkNewRecipe;

    public OvenBlockEntity(BlockPos pos, BlockState state){
        super(ModBlockEntityTypes.OVEN.get(),pos,state);
        inventory = createHandler();
        this.inputHandler = LazyOptional.of(() -> new CookingOvenItemHandler(inventory, Direction.UP));
        this.outputHandler = LazyOptional.of(() -> new CookingOvenItemHandler(inventory,Direction.DOWN));
        this.mealContainerStack = ItemStack.EMPTY;
        this.cookingOvenData = createInArray();
        this.usedRecipeTracker = new Object2IntOpenHashMap<>();
        this.checkNewRecipe = true;

    }

    public static ItemStack getMealFromItem(ItemStack cookingOvenStack){
        if(!cookingOvenStack.is(ModItem.OVEN.get())) {
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

    public static void takeServingFromItem(ItemStack cookingOvenStack) {
        if (!cookingOvenStack.is(ModItem.OVEN.get())) {
            return;
        }

        CompoundTag compound = cookingOvenStack.getTagElement("BlockEntityTag");
        if (compound != null) {
            CompoundTag inventoryTag = compound.getCompound("Inventory");
            if (inventoryTag.contains("Items", 9)) {
                ItemStackHandler handler = new ItemStackHandler();
                handler.deserializeNBT(inventoryTag);
                ItemStack newMealStack = handler.getStackInSlot(6);
                newMealStack.shrink(1);
                compound.remove("Inventory");
                compound.put("Inventory", handler.serializeNBT());
            }
        }
    }

    public static ItemStack getContainerFromItem(ItemStack cookingOvenStack){
        if(!cookingOvenStack.is(ModItem.OVEN.get())) {
            return ItemStack.EMPTY;
        }

        CompoundTag compound = cookingOvenStack.getTagElement("BlockEntityTag");
        if(compound != null){
            return ItemStack.of(compound.getCompound("Container"));
        }
        return ItemStack.EMPTY;
    }

    public boolean isLit(BlockState state){
        return state.getValue(LIT);
    }

    @SuppressWarnings("all")
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        cookTime = compound.getInt("CookTime");
        cookTimeTotal = compound.getInt("CookTimeTotal");
        mealContainerStack = ItemStack.of(compound.getCompound("Container"));
        if (compound.contains("CustomName", 8)) {
            customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }
        CompoundTag compoundRecipes = compound.getCompound("RecipesUsed");

        for (String key : compoundRecipes.getAllKeys()) {
            usedRecipeTracker.put(new ResourceLocation(key), compoundRecipes.getInt(key));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("CookTime", cookTime);
        compound.putInt("CookTimeTotal", cookTimeTotal);
        compound.put("Container", mealContainerStack.serializeNBT());
        if (customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(customName));
        }
        compound.put("Inventory", inventory.serializeNBT());
        CompoundTag compoundRecipes = new CompoundTag();
        usedRecipeTracker.forEach((recipeId, craftedAmount) -> compoundRecipes.putInt(recipeId.toString(), craftedAmount));
        compound.put("RecipesUsed", compoundRecipes);
    }

    private CompoundTag writeItems(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Container", mealContainerStack.serializeNBT());
        compound.put("Inventory", inventory.serializeNBT());
        return compound;
    }

    public CompoundTag writeMeal(CompoundTag compound) {
        if (getMeal().isEmpty()) return compound;

        ItemStackHandler drops = new ItemStackHandler(inventory_size);
        for (int i = 0; i < inventory_size; ++i) {
            drops.setStackInSlot(i, i == meal_display_slot ? inventory.getStackInSlot(i) : ItemStack.EMPTY);
        }
        if (customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(customName));
        }
        compound.put("Container", mealContainerStack.serializeNBT());
        compound.put("Inventory", drops.serializeNBT());
        return compound;
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state,OvenBlockEntity ovenBlock) {
        boolean didInventoryChange = false;

        if (ovenBlock.hasInput()) {
            Optional<OvenBlockRecipe> recipe = ovenBlock.getMatchingRecipe(new RecipeWrapper(ovenBlock.inventory));
            if (recipe.isPresent() && ovenBlock.canCook(recipe.get())) {
                didInventoryChange = ovenBlock.processCooking(recipe.get(), ovenBlock);
            } else {
                ovenBlock.cookTime = Mth.clamp(ovenBlock.cookTime - 2, 0, ovenBlock.cookTimeTotal);
            }
        } else if (ovenBlock.cookTime > 0) {
            ovenBlock.cookTime = Mth.clamp(ovenBlock.cookTime - 2, 0, ovenBlock.cookTimeTotal);
        }

        ItemStack mealStack = ovenBlock.getMeal();
        if (!mealStack.isEmpty()) {
            if (!ovenBlock.doesMealHaveContainer(mealStack)) {
                ovenBlock.moveMealToOutput();
                didInventoryChange = true;
            } else if (!ovenBlock.inventory.getStackInSlot(container_slot).isEmpty()) {
                ovenBlock.useStoredContainersOnMeal();
                didInventoryChange = true;
            }
        }

        if (didInventoryChange) {
            ovenBlock.inventoryChanged();
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, OvenBlockEntity blockEntity) {
        if (level.isClientSide && state.getValue(LIT)) {
            blockEntity.animationTick++;

            if (blockEntity.animationTick >= ANIMATION_INTERVAL) {
                boolean currentAnimate = state.getValue(ANIMATE);
                level.setBlock(pos, state.setValue(ANIMATE, !currentAnimate), 3);
                blockEntity.animationTick = 0;
            }
        }
    }

    private Optional<OvenBlockRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
        if (level == null) return Optional.empty();

        if (lastRecipeID != null) {
            Recipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
                    .getRecipeMap(ModRecipeTypes.COOKING_BY_OVEN.get())
                    .get(lastRecipeID);
            if (recipe instanceof OvenBlockRecipe) {
                if (recipe.matches(inventoryWrapper, level)) {
                    return Optional.of((OvenBlockRecipe) recipe);
                }
                if (ItemStack.isSameItem(recipe.getResultItem(this.level.registryAccess()), getMeal())) {
                    return Optional.empty();
                }
            }
        }

        if (checkNewRecipe) {
            Optional<OvenBlockRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.COOKING_BY_OVEN.get(), inventoryWrapper, level);
            if (recipe.isPresent()) {
                ResourceLocation newRecipeID = recipe.get().getId();
                if (lastRecipeID != null && !lastRecipeID.equals(newRecipeID)) {
                    cookTime = 0;
                }
                lastRecipeID = newRecipeID;
                return recipe;
            }
        }

        checkNewRecipe = false;
        return Optional.empty();
    }

    public ItemStack getContainer(){
        ItemStack mealStack = getMeal();
        if (mealStack.isEmpty() || mealContainerStack.isEmpty()) return mealStack.getCraftingRemainingItem();
        return mealContainerStack;
    }

    private boolean hasInput() {
        for (int i = 0; i < meal_display_slot; ++i) {
            if (!inventory.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    protected boolean canCook(OvenBlockRecipe recipe) {
        if (hasInput()) {
            ItemStack resultStack = recipe.getResultItem(this.level.registryAccess());
            if (resultStack.isEmpty()) {
                return false;
            } else {
                ItemStack storedMealStack = inventory.getStackInSlot(meal_display_slot);
                if (storedMealStack.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(storedMealStack, resultStack)) {
                    return false;
                } else if (storedMealStack.getCount() + resultStack.getCount() <= inventory.getSlotLimit(meal_display_slot)) {
                    return true;
                } else {
                    return storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private boolean processCooking(OvenBlockRecipe recipe, OvenBlockEntity ovenBlock) {
        if (level == null) return false;

        ++cookTime;
        cookTimeTotal = recipe.getCookTime();
        if (cookTime < cookTimeTotal) {
            return false;
        }

        cookTime = 0;
        mealContainerStack = recipe.getOutputContainer();
        ItemStack resultStack = recipe.getResultItem(this.level.registryAccess());
        ItemStack storedMealStack = inventory.getStackInSlot(meal_display_slot);
        if (storedMealStack.isEmpty()) {
            inventory.setStackInSlot(meal_display_slot, resultStack.copy());
        } else if (ItemStack.isSameItem(storedMealStack, resultStack)) {
            storedMealStack.grow(resultStack.getCount());
        }
        ovenBlock.setRecipeUsed(recipe);

        for (int i = 0; i < meal_display_slot; ++i) {
            ItemStack slotStack = inventory.getStackInSlot(i);
            if (slotStack.hasCraftingRemainingItem()) {
                ejectIngredientRemainder(slotStack.getCraftingRemainingItem());
            } else if (INGREDIENT_REMAINDER_OVERRIDES.containsKey(slotStack.getItem())) {
                ejectIngredientRemainder(INGREDIENT_REMAINDER_OVERRIDES.get(slotStack.getItem()).getDefaultInstance());
            }
            if (!slotStack.isEmpty())
                slotStack.shrink(1);
        }
        return true;
    }

    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        Direction direction = getBlockState().getValue(OvenBlock.FACING).getCounterClockWise();
        double x = worldPosition.getX() + 0.5 + (direction.getStepX() * 0.25);
        double y = worldPosition.getY() + 0.7;
        double z = worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.25);
        ItemUtils.spawnItemEntity(level, remainderStack, x, y, z,
                direction.getStepX() * 0.08F, 0.25F, direction.getStepZ() * 0.08F);
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation recipeID = recipe.getId();
            usedRecipeTracker.addTo(recipeID, 1);
        }
    }

    @Override
    @Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> items) {
        List<Recipe<?>> usedRecipes = getUsedRecipesAndPopExperience(player.level(), player.position());
        player.awardRecipes(usedRecipes);
        usedRecipeTracker.clear();
    }

    public List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        List<Recipe<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : usedRecipeTracker.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                splitAndSpawnExperience((ServerLevel) level, pos, entry.getIntValue(), ((OvenBlockRecipe) recipe).getExperience());
            });
        }

        return list;
    }

    private static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
        int expTotal = Mth.floor((float) craftedAmount * experience);
        float expFraction = Mth.frac((float) craftedAmount * experience);
        if (expFraction != 0.0F && Math.random() < (double) expFraction) {
            ++expTotal;
        }

        ExperienceOrb.award(level, pos, expTotal);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStack getMeal() {
        return inventory.getStackInSlot(meal_display_slot);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < inventory_size; ++i) {
            if (i != meal_display_slot) {
                drops.add(inventory.getStackInSlot(i));
            }
        }
        return drops;
    }

    private void moveMealToOutput() {
        ItemStack mealStack = inventory.getStackInSlot(meal_display_slot);
        ItemStack outputStack = inventory.getStackInSlot(output_slot);
        int mealCount = Math.min(mealStack.getCount(), mealStack.getMaxStackSize() - outputStack.getCount());
        if (outputStack.isEmpty()) {
            inventory.setStackInSlot(output_slot, mealStack.split(mealCount));
        } else if (outputStack.getItem() == mealStack.getItem()) {
            mealStack.shrink(mealCount);
            outputStack.grow(mealCount);
        }
    }

    private void useStoredContainersOnMeal() {
        ItemStack mealStack = inventory.getStackInSlot(meal_display_slot);
        ItemStack containerInputStack = inventory.getStackInSlot(container_slot);
        ItemStack outputStack = inventory.getStackInSlot(output_slot);

        if (isContainerValid(containerInputStack) && outputStack.getCount() < outputStack.getMaxStackSize()) {
            int smallerStackCount = Math.min(mealStack.getCount(), containerInputStack.getCount());
            int mealCount = Math.min(smallerStackCount, mealStack.getMaxStackSize() - outputStack.getCount());
            if (outputStack.isEmpty()) {
                containerInputStack.shrink(mealCount);
                inventory.setStackInSlot(output_slot, mealStack.split(mealCount));
            } else if (outputStack.getItem() == mealStack.getItem()) {
                mealStack.shrink(mealCount);
                containerInputStack.shrink(mealCount);
                outputStack.grow(mealCount);
            }
        }
    }

    public ItemStack useHeldItemOnMeal(ItemStack container) {
        if (isContainerValid(container) && !getMeal().isEmpty()) {
            container.shrink(1);
            inventoryChanged();
            return getMeal().split(1);
        }
        return ItemStack.EMPTY;
    }

    private boolean doesMealHaveContainer(ItemStack meal) {
        return !mealContainerStack.isEmpty() || meal.hasCraftingRemainingItem();
    }

    public boolean isContainerValid(ItemStack containerItem) {
        if (containerItem.isEmpty()) return false;
        if (!mealContainerStack.isEmpty()) return ItemStack.isSameItem(mealContainerStack, containerItem);
        return ItemStack.isSameItem(getMeal(), containerItem);
    }

    @Override
    public Component getName() {
        return customName != null ? customName : TextUtils.getTranslation("container.oven");
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    @Nullable
    public Component getCustomName() {
        return customName;
    }

    public void setCustomName(Component name) {
        customName = name;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new OvenBlockMenu(id, player, this, cookingOvenData);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
            if (side == null || side.equals(Direction.UP)) {
                return inputHandler.cast();
            } else {
                return outputHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inputHandler.invalidate();
        outputHandler.invalidate();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return writeItems(new CompoundTag());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(inventory_size)
        {
            @Override
            protected void onContentsChanged(int slot) {
                if (slot >= 0 && slot < meal_display_slot) {
                    checkNewRecipe = true;
                }
                inventoryChanged();
            }
        };
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