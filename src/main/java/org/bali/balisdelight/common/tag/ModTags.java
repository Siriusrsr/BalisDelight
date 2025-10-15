package org.bali.balisdelight.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bali.balisdelight.BalisDelight;

public class ModTags {

//    public static final TagKey<Item> MEALS = modItemTag("meals");
//
//    public static final TagKey<Item> DRINKS = modItemTag("drinks");

    @SuppressWarnings("all")
    private static TagKey<Item> modItemTag(String path) {
        return ItemTags.create(new ResourceLocation(BalisDelight.MOD_ID, path));
    }

    private static TagKey<Block> modBlockTag(String path) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(BalisDelight.MOD_ID, path));
    }

    private static TagKey<EntityType<?>> modEntityTag(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(BalisDelight.MOD_ID, path));
    }
////用于烤箱烤制菜肴的承装容器
    public static final TagKey<Item> OVEN_SERVING_CONTAINERS = modItemTag("oven_serving_containers");
////使用咖啡制成的咖啡制品，给予特殊药水效果驱散幻翼
    public static final TagKey<Item> COFFEE_PRODUCT = modItemTag("coffee_product");
}
