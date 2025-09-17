package org.bali.balisdelight;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.bali.balisdelight.common.registry.ModBlock;
import org.bali.balisdelight.common.registry.ModItem;


@Mod("balisdelight")
public class BalisDelight {

    public static final String MOD_ID = "balisdelight";

    public BalisDelight(FMLJavaModLoadingContext context) {
        ModItem.ITEMS.register(context.getModEventBus());
        ModItem.TABS.register(context.getModEventBus());
        ModBlock.BLOCKS.register(context.getModEventBus());
    }

    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {

    }
}

