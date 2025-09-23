package org.bali.balisdelight;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.bali.balisdelight.common.registry.ModBlock;
import org.bali.balisdelight.common.registry.ModBlockEntityTypes;
import org.bali.balisdelight.common.registry.ModItem;
import org.bali.balisdelight.common.registry.ModTab;


@Mod("balisdelight")
public class BalisDelight {
    /**
     * 本模组的MOD_ID
     */
    public static final String MOD_ID = "balisdelight";

    /**
     * 调用该方法将模组内容注册到游戏中
     * @param context FML上下文
     */
    public BalisDelight(FMLJavaModLoadingContext context) {
        ModBlock.BLOCKS.register(context.getModEventBus());
        ModItem.ITEMS.register(context.getModEventBus());
        ModTab.TABS.register(context.getModEventBus());
        ModBlockEntityTypes.TILES.register(context.getModEventBus());
    }

    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {

    }
}

