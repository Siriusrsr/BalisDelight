package org.bali.balisdelight.common.registry;

import com.google.common.collect.Sets;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.bali.balisdelight.common.item.CoffeeBeans;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

/**
 * 本模组的物品注册类。所有物品注册应在此类中完成
 */
public class ModItem {

    /**
     * 本模组的Item注册Handler
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "balisdelight");
    public static LinkedHashSet<RegistryObject<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    /**
     * 本模组的Item标准化添加到创造模式物品栏
     */
    public static RegistryObject<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
        RegistryObject<Item> block = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(block);
        return block;
    }

    /**
     * 本模组的Item标准化分类
     */
    public static Item.Properties commonItem() {
        return new Item.Properties();
    }

    public static Item.Properties foodItem(FoodProperties food) {
        return new Item.Properties().food(food);
    }

    public static final RegistryObject<Item> COFFEE_BEANS = registerWithTab("coffee_beans", CoffeeBeans::new);//coffee_beans注册obj

    public static final RegistryObject<Item> STONE_POT = registerWithTab("stone_pot",
            ()-> new BlockItem(ModBlock.STONE_POT.get(), commonItem()));

    public static final RegistryObject<Item> CRISPY_CREAM_MUSHROOM_SOUP = registerWithTab("crispy_cream_mushroom_soup",
            ()-> new BlockItem(ModBlock.CRISPY_CREAM_MUSHROOM_SOUP.get(), commonItem()));

    public static final RegistryObject<Item> OVEN = registerWithTab("oven",
            ()-> new BlockItem(ModBlock.OVEN.get(), commonItem()));

    public static final RegistryObject<Item> TEST_BLOCK = ITEMS.register("test_block",
            () -> new BlockItem(ModBlock.TEST_BLOCK.get(), new Item.Properties()));
}
