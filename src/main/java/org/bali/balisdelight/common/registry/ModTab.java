package org.bali.balisdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static org.bali.balisdelight.BalisDelight.MOD_ID;

/**
 * 本模组的物品注册类。所有物品注册应在此类中完成
 */
public class ModTab {

    /**
     * 本模组的TAB注册Handler
     */
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "balisdelight");

    /**
     * tag定义
     */
    static final CreativeModeTab ct = CreativeModeTab.builder()
            .title(Component.translatable("item_group."+MOD_ID+".BalisDelightTab"))
            .icon(() -> new ItemStack(ModItem.coffee_beans.get()))
            .displayItems((params, output) -> ModItem.CREATIVE_TAB_ITEMS.forEach((item)-> output.accept(item.get())))
            .build();
    //tag注册obj
    static final RegistryObject<CreativeModeTab> balis_delight_tab = TABS.register("balisdelighttab",()->ct);
}
