package org.bali.balisdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.bali.balisdelight.common.block.entity.OvenBlockEntity;
import org.bali.balisdelight.common.block.state.OvenBlockSupport;
import org.bali.balisdelight.common.registry.ModBlockEntityTypes;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class OvenBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<OvenBlockSupport> SUPPORT = EnumProperty.create("support", OvenBlockSupport.class);

    private static final VoxelShape voxelShape;

    static {
        VoxelShape base = Block.box(0.0D,0.0D,1.0D,16.0D,16.D,16.0D);
        voxelShape = Shapes.or(base);
    }

    public OvenBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

        @Override
        public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                     InteractionHand hand, BlockHitResult result) {
            ItemStack heldStack = player.getItemInHand(hand);
            if (heldStack.isEmpty() && player.isShiftKeyDown()) {
                level.setBlockAndUpdate(pos,defaultBlockState());
                level.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
            } else if (!level.isClientSide) {
                BlockEntity tileEntity = level.getBlockEntity(pos);
                if (tileEntity instanceof OvenBlockEntity ovenEntity) {
                    ItemStack servingStack = ovenEntity.useHeldItemOnMeal(heldStack);
                    if (servingStack != ItemStack.EMPTY) {
                        if (!player.getInventory().add(servingStack)) {
                            player.drop(servingStack, false);
                        }
                        level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.BLOCKS, 1.0F, 1.0F);
                    } else {
                        NetworkHooks.openScreen((ServerPlayer) player, ovenEntity, pos);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.SUCCESS;
        }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context){
        return voxelShape;
    }

    /**
     *  在放置方块时，根据玩家朝向改变方块的方向值
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        return this.defaultBlockState().setValue(FACING,context.getHorizontalDirection());
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        OvenBlockEntity ovenBlockEntity = (OvenBlockEntity) level.getBlockEntity(pos);
        if (ovenBlockEntity != null) {
            CompoundTag nbt = ovenBlockEntity.writeMeal(new CompoundTag());
            if (!nbt.isEmpty()) {
                stack.addTagElement("BlockEntityTag", nbt);
            }
            if (ovenBlockEntity.hasCustomName()) {
                stack.setHoverName(ovenBlockEntity.getCustomName());
            }
        }
        return stack;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return ModBlockEntityTypes.OVEN.get().create(pos, state);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>)ticker : null;
    }
}

