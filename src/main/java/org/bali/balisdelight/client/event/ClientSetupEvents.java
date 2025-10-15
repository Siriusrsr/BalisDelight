package org.bali.balisdelight.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.bali.balisdelight.BalisDelight;
import org.bali.balisdelight.client.gui.OvenBlockTooltip;
import org.bali.balisdelight.client.recipebook.RecipeCategories;

@Mod.EventBusSubscriber(modid = BalisDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEvents {
    @SubscribeEvent
    public static void onRegisterRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        System.out.println("=== 注册配方书分类 ===");
        RecipeCategories.init(event);
    }

    @SubscribeEvent
    public static void registerCustomTooltipRenderers(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(OvenBlockTooltip.CookingPotTooltipComponent.class, OvenBlockTooltip::new);
    }
}
