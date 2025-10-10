package org.bali.balisdelight.common.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.common.block.entity.container.OvenBlockMenu;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, BalisDelight.MOD_ID);

    public static final RegistryObject<MenuType<OvenBlockMenu>> OVEN_BLOCK = MENU_TYPES
            .register("oven_block",()-> IForgeMenuType.create(OvenBlockMenu::new));
}
