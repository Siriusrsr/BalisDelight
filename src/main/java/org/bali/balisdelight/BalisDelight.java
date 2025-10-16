package org.bali.balisdelight;

import net.minecraft.world.inventory.RecipeBookType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bali.balisdelight.client.ClientSetup;
import org.bali.balisdelight.common.Configuration;
import org.bali.balisdelight.common.registry.*;


@Mod("balisdelight")
public class BalisDelight {

    /**
     * 本模组的MOD_ID
     */
    public static final String MOD_ID = "balisdelight";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final RecipeBookType RECIPE_BOOK_TYPE = RecipeBookType.create("COOKING_BY_OVEN");
    /**
     * 调用该方法将模组内容注册到游戏中
     * @param context FML上下文
     */
    @SuppressWarnings("all")
    public BalisDelight(FMLJavaModLoadingContext context) {


        if (FMLEnvironment.dist.isClient()) {
        context.getModEventBus().addListener(ClientSetup::init);
        }

        initMixinConfig();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_CONFIG);

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

    private void initMixinConfig() {
        // 确保 Mixin 配置被加载
        try {
            Class.forName("org.spongepowered.asm.mixin.Mixins");
            LOGGER.info("Mixin 系统已初始化");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Mixin 类未找到，可能配置有问题");
        }
    }

}

