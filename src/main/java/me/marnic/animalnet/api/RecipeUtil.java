package me.marnic.animalnet.api;

import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class RecipeUtil {

    public static ItemStack getCaughtEntityFixedStack() {
        ItemStack stack = new ItemStack(AnimalNetItems.caughtEntityItem);
        stack.setCustomName(new LiteralText("Caught Entity").formatted(Formatting.YELLOW));
        return stack;
    }

    public static boolean isNetWithData(ItemStack stack) {
        return stack.getItem().equals(AnimalNetItems.caughtEntityItem) && (stack.hasTag() ? stack.getTag().containsKey("animalName") : false);
    }
}
