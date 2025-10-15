package org.bali.balisdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bali.balisdelight.common.registry.ModBlock;

public class CrispyCreamMushroomSoup extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 4);

    private static final VoxelShape voxelShape;

    static {
        VoxelShape base = Block.box(2.0D,0.0D,2.0D,14.0D,4.0D,14.0D);
        VoxelShape top = Block.box(1.0D,5.0D,1.0D,15.0D,6.0D,15.0D);

        voxelShape = Shapes.or(base,top);
    }

    public CrispyCreamMushroomSoup(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BITES,0));
    }

    @Override
    @SuppressWarnings("unused")
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){
                ItemStack heldStack = player.getItemInHand(hand);
        if (level.isClientSide) {

            if (this.consumeBite(level, pos, state, player) == InteractionResult.SUCCESS) {
                return InteractionResult.SUCCESS;
            }

            if (heldStack.isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }
        return consumeBite(level,pos,state,player);
    }

    protected InteractionResult consumeBite(Level level,BlockPos pos,BlockState state,Player playerIn){
        if (!playerIn.canEat(false)) {
            return InteractionResult.PASS;
        }else {
            playerIn.getFoodData().eat(2,0.3f);
            int bites = state.getValue(BITES);
            if (bites == 4) {
                level.setBlock(pos,ModBlock.STONE_POT.get().defaultBlockState().setValue(FACING,state.getValue(FACING)), 3);
                level.playSound(null,pos,SoundEvents.GENERIC_DRINK,SoundSource.BLOCKS,0.8f,0.8f);
            }

            else {
                level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
                level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            }
            return InteractionResult.SUCCESS;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BITES);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type){
        return false;
    }
}
