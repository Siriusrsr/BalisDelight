package org.bali.balisdelight;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod("balisdelight")
public class BalisDelight {
    /**
     * 本模组的MOD_ID
     */
    public static final String MOD_ID = "balisdelight";
    public BalisDelight(FMLJavaModLoadingContext context) {
        BalisRegistry.registry(context);
    }

    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {

    }
}

