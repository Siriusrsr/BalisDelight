package org.bali.balisdelight.common.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bali.balisdelight.common.block.CrispyCreamMushroomSoup;
import org.bali.balisdelight.common.block.OvenBlock;
import org.bali.balisdelight.common.block.StonePot;
import org.bali.balisdelight.common.block.TestBlock;

import static org.bali.balisdelight.BalisDelight.MOD_ID;

public class ModBlock {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static  final RegistryObject<Block> STONE_POT = BLOCKS.register("stone_pot",
            ()-> new StonePot(Block.Properties.copy(Blocks.STONE)));
    public static  final RegistryObject<Block> CRISPY_CREAM_MUSHROOM_SOUP = BLOCKS.register("crispy_cream_mushroom_soup",
            ()-> new CrispyCreamMushroomSoup(Block.Properties.copy(Blocks.CAKE)
                    .strength(0.5f, 0.5f)));
    public static  final RegistryObject<Block> OVEN = BLOCKS.register("oven",
            ()-> new OvenBlock(Block.Properties.copy(Blocks.IRON_BLOCK)));
    public static final  RegistryObject<Block> TEST_BLOCK = BLOCKS.register("test_block",
            ()-> new TestBlock(Block.Properties.copy(Blocks.IRON_BLOCK)));

}
