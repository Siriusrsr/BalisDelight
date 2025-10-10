package org.bali.balisdelight.common.item;

import net.minecraft.world.item.Item;

public class CoffeeBeans extends Item {

    public CoffeeBeans() {
        //Magic! The coffee beans can resist the fire!!!
        super(new Properties().fireResistant());
    }
}


