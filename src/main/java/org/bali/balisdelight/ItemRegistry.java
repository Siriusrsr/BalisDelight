package org.bali.balisdelight;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static org.bali.balisdelight.BalisDelight.MOD_ID;

public class ItemRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "balisdelight");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "balisdelight");
    public static RegistryObject<Item> coffee_beans = ITEMS.register("coffee_beans", ()-> new CoffeeBeans());
    public static final CreativeModeTab ct = CreativeModeTab.builder()
            .title(Component.translatable("item_group."+MOD_ID+".BalisDelightTab"))
            .icon(() -> new ItemStack(coffee_beans.get()))
            .displayItems((params, output) -> {
                output.accept(coffee_beans.get());})
            .build();
    public static final RegistryObject<CreativeModeTab> balis_delight_tab = TABS.register("balisdelighttab",()->ct);
}
