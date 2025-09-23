package org.bali.balisdelight.common.Block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


    public class StonePot extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape voxelShape;

    /**
     *  定义方块模型
     */
    static {
        VoxelShape base = Block.box(2.0D,0.0D,2.0D,14.0D,1.0D,14.0D);
        VoxelShape line1 = Block.box(2.0D,1.0D,2.0D,3.0D,4.0D,14.0D);
        VoxelShape line2 = Block.box(3.0D,1.0D,2.0D,13.0D,4.0D,3.0D);
        VoxelShape line3 = Block.box(3.0D,1.0D,13.0D,13.0D,4.0D,14.0D);
        VoxelShape line4 = Block.box(13.0D,1.0D,2.0D,14.0D,4.0D,14.0D);
        voxelShape = Shapes.or(base,line1,line2,line3,line4);
    }

    public StonePot(Properties properties) {
        super(Properties.of());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    /**
     *  使用新模型替换碰撞箱
    */
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder .add(FACING);
    }
}
