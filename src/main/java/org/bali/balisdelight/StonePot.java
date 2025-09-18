package org.bali.balisdelight;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class StonePot extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 15.0D, 4.0D, 15.0D);
//    private static final VoxelShape SHAPE,
//    static {
//        VoxelShape base = Block.box(1.0D,0.0D,1.0D,15.0D,1.0D,15.0D);
//        VoxelShape line1 = Block.box(1.0D,1.0D,1.0D,2.0D,4.0D,15.0D);
//        VoxelShape line2 = Block.box(2.0D,1.0D,1.0D,14.0D,4.0D,2.0D);
//        VoxelShape line3 = Block.box(2.0D,1.0D,14.0D,14.0D,4.0D,15.0D);
//        VoxelShape line4 = Block.box(14.0D,1.0D,1.0D,15.0D,4.0D,15.0D);
//        voxelShape = Shapes.or(base,line1,line2,line3,line4);
//    }

    public StonePot(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    /**
    *Shape the block with the new model.
    */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context){
        return SHAPE;
    }

    /**
    *Change the facing direction of the block while it is placed by the player.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        return this.defaultBlockState().setValue(FACING,context.getHorizontalDirection());
    }
}
