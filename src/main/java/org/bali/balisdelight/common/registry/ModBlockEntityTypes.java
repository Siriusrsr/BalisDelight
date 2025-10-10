package org.bali.balisdelight.common.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.common.block.entity.OvenBlockEntity;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BalisDelight.MOD_ID);

    public static final RegistryObject<BlockEntityType<OvenBlockEntity>> OVEN = TILES.register("oven",
            ()->BlockEntityType.Builder.of(OvenBlockEntity::new, ModBlock.OVEN.get()).build(null));
}
