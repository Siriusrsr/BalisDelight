package org.bali.balisdelight.common.Block.Entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SyncedBlockEntity extends BlockEntity {
    public SyncedBlockEntity(BlockEntityType<?> tileEntityTypeIn,BlockPos pos, BlockState state){
        super(tileEntityTypeIn,pos,state);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

//    @Override
    public CompoundTag getUpdatedTag(){
        return saveWithoutMetadata();
    }

//    @Override
    public void onDataPacked(Connection net, ClientboundBlockEntityDataPacket pkt){
        if (pkt.getTag() != null) {
            load(pkt.getTag());
        }
    }

    protected void inventoryChanged(){
        super.setChanged();
        if(level !=null)
            level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(), Block.UPDATE_CLIENTS);
    }
}
