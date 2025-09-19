package org.bali.balisdelight.common.registry;

import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.bali.balisdelight.CoffeeBeans;
import org.bali.balisdelight.StonePot;
import org.objectweb.asm.tree.IincInsnNode;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

import static org.bali.balisdelight.BalisDelight.MOD_ID;

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
    public static Item.Properties CommonItem() {
        return new Item.Properties();
    }

    static final RegistryObject<Item> coffee_beans = registerWithTab("coffee_beans", CoffeeBeans::new);//coffee_beans注册obj

    static final RegistryObject<Item> stone_pot = registerWithTab("stone_pot",()->{
        return new BlockItem(ModBlock.stone_pot.get(), CommonItem());
    });

    static final RegistryObject<Item> crispy_cream_mushroom_soup = registerWithTab("crispy_cream_mushroom_soup", ()->{
        return new BlockItem(ModBlock.crispy_cream_mushroom_soup.get(), CommonItem());
    });
}
