package me.marnic.animalnet.recipes;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.IngredientNBT;

import javax.annotation.Nullable;

/**
 * Copyright (c) 24.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CustomIngredient extends IngredientNBT {
    protected CustomIngredient(ItemStack stack) {
        super(stack);
    }

    @Override
    public boolean apply(@Nullable ItemStack input) {
        return true;
    }
}
