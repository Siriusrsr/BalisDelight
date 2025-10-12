package org.bali.balisdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.common.registry.ModBlock;
import org.bali.balisdelight.common.tag.ModTags;

import javax.annotation.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTags extends BlockTagsProvider {
    public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BalisDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.registerModTags();
//        this.registerMinecraftTags();
//        this.registerForgeTags();
//        this.registerCompatibilityTags();

        this.registerBlockMineables();
    }

    protected void registerBlockMineables() {
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlock.OVEN.get())
                .add(ModBlock.STONE_POT.get()
        );
    }

    protected void registerModTags() {

    }
}
