package org.bali.balisdelight;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

 public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS,"balisdelight");
    public static RegistryObject<Item> coffee_beans = ITEMS.register("coffee_beans",() -> {
        return new CoffeeBeans();
    });
}{
}
