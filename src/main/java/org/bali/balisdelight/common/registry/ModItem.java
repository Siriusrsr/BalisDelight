package org.bali.balisdelight.common.registry;

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


import static org.bali.balisdelight.BalisDelight.MOD_ID;

/**
 * 本模组的注册类。所有注册应在此类中完成
 */
public class ModItem {
    /**
     * 本模组的TAB注册Handler
     */
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "balisdelight");
    /**
     * 本模组的Item注册Handler
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "balisdelight");

    static final RegistryObject<Item> coffee_beans = ITEMS.register("coffee_beans", CoffeeBeans::new);//coffee_beans注册obj
    static final RegistryObject<Item> stone_pot = ITEMS.register("stone_pot",()->{
        return new BlockItem(ModBlock.stone_pot.get(), new Item.Properties());
    });
    static final RegistryObject<Item> crispy_cream_mushroom_soup = ITEMS.register("crispy_cream_mushroom_soup", ()->{
        return new BlockItem(ModBlock.crispy_cream_mushroom_soup.get(), new Item.Properties());
    });

    /**
     * tag定义
     */
    static final CreativeModeTab ct = CreativeModeTab.builder()
            .title(Component.translatable("item_group."+MOD_ID+".BalisDelightTab"))
            .icon(() -> new ItemStack(coffee_beans.get()))
            .displayItems((params, output) -> {
                output.accept(coffee_beans.get());
                output.accept(crispy_cream_mushroom_soup.get());
                output.accept(stone_pot.get());
            })
            .build();
    //tag注册obj
    static final RegistryObject<CreativeModeTab> balis_delight_tab = TABS.register("balisdelighttab",()->ct);

    /**
     * 调用该方法将模组内容注册到游戏中
     * @param context FML上下文
     */
    public static void registry(FMLJavaModLoadingContext context){
        ITEMS.register(context.getModEventBus());
        TABS.register(context.getModEventBus());
    }
}
