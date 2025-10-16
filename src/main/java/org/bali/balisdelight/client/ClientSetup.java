package org.bali.balisdelight.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.bali.balisdelight.client.gui.OvenBlockScreen;
import org.bali.balisdelight.common.registry.ModMenuTypes;

public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        System.out.println("注册烤箱屏幕");
        event.enqueueWork(() -> MenuScreens.register(ModMenuTypes.OVEN_BLOCK.get(), OvenBlockScreen::new));
    }

}