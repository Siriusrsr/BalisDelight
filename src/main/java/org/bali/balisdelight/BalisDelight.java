package org.bali.balisdelight;

import net.minecraft.world.inventory.RecipeBookType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bali.balisdelight.client.ClientSetup;
import org.bali.balisdelight.common.registry.*;


@Mod("balisdelight")
public class BalisDelight {


    /**
     * 本模组的MOD_ID
     */
    public static final String MOD_ID = "balisdelight";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final RecipeBookType RECIPE_BOOK_TYPE = RecipeBookType.create("OvenCooking");
    /**
     * 调用该方法将模组内容注册到游戏中
     * @param context FML上下文
     */
    public BalisDelight(FMLJavaModLoadingContext context) {
        if (FMLEnvironment.dist.isClient()) {
        context.getModEventBus().addListener(ClientSetup::init);
        }

        ModBlock.BLOCKS.register(context.getModEventBus());
        ModBlockEntityTypes.TILES.register(context.getModEventBus());
        ModItem.ITEMS.register(context.getModEventBus());
        ModTab.TABS.register(context.getModEventBus());
        ModMenuTypes.MENU_TYPES.register(context.getModEventBus());
        ModRecipeTypes.RECIPE_TYPES.register(context.getModEventBus());
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(context.getModEventBus());
    }

    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {

    }

}

