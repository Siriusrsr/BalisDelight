package org.bali.balisdelight.common.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static org.bali.balisdelight.BalisDelight.MOD_ID;

public class ModBlock {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static  final RegistryObject<Block> crispy_cream_mushroom_soup = BLOCKS.register("crispy_cream_mushroom_soup",
            ()-> new Block(BlockBehaviour.Properties.of().destroyTime(5).mapColor(MapColor.CLAY)));
}
