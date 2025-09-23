package org.bali.balisdelight.common.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bali.balisdelight.common.Block.CrispyCreamMushroomSoup;
import org.bali.balisdelight.common.Block.OvenBlock;
import org.bali.balisdelight.common.Block.StonePot;

import static org.bali.balisdelight.BalisDelight.MOD_ID;

public class ModBlock {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static  final RegistryObject<Block> stone_pot = BLOCKS.register("stone_pot",
            ()-> new StonePot(Block.Properties.copy(Blocks.STONE)));
    public static  final RegistryObject<Block> crispy_cream_mushroom_soup = BLOCKS.register("crispy_cream_mushroom_soup",
            ()-> new CrispyCreamMushroomSoup(Block.Properties.copy(Blocks.CAKE)));
    public static  final RegistryObject<Block> oven = BLOCKS.register("oven",
            ()-> new OvenBlock(Block.Properties.copy(Blocks.IRON_BLOCK)));

}
