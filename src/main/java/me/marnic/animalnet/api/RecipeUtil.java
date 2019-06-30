package me.marnic.animalnet.api;

import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class RecipeUtil {

    public static ItemStack getCaughtEntityFixedStack() {
        ItemStack stack = new ItemStack(AnimalNetItems.caughtEntityItem);
        stack.setDisplayName(new StringTextComponent("Caught Entity").applyTextStyle(TextFormatting.YELLOW));
        return stack;
    }

    public static boolean isNetWithData(ItemStack stack) {
        return stack.getItem().equals(AnimalNetItems.caughtEntityItem) && (stack.hasTag() ? stack.getTag().contains("animalName") : false);
    }
}
