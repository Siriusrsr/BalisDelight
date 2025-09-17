package org.bali.balisdelight;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.targets.FMLClientDevLaunchHandler;

@Mod("balisdelight")
public class BalisDelight {
    public BalisDelight() {
        ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}

