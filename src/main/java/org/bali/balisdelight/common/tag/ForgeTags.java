package org.bali.balisdelight.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ForgeTags {


    public static final TagKey<Item> MILK = forgeItemTag("milk");
    public static final TagKey<Item> MILK_BUCKET = forgeItemTag("milk/milk");
    public static final TagKey<Item> MILK_BOTTLE = forgeItemTag("milk/milk_bottle");

    @SuppressWarnings("all")
    private static TagKey<Block> forgeBlockTag(String path) {
        return BlockTags.create(new ResourceLocation("forge", path));
    }
    @SuppressWarnings("all")
    private static TagKey<Item> forgeItemTag(String path) {
        return ItemTags.create(new ResourceLocation("forge", path));
    }
}
