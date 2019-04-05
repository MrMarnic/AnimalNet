package me.marnic.animalnet.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Copyright (c) 05.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ItemStackHelper {
    public static boolean hasType(ItemStack stack, Item item) {
        if(stack!=null) {
            return stack.getItem().equals(item);
        }
        return false;
    }
}
