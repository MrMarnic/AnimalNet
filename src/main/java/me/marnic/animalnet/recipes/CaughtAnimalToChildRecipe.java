package me.marnic.animalnet.recipes;

import me.marnic.animalnet.api.ItemStackHelper;
import me.marnic.animalnet.item.CaughtEntityItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

/**
 * Copyright (c) 05.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CaughtAnimalToChildRecipe implements IRecipe {

    private ItemStack match;

    private final ItemStack out = new ItemStack(AnimalNetItems.caughtEntityItem);

    @Override
    public boolean matches(InventoryCrafting inv, World p_77569_2_) {
        if (ItemStackHelper.hasType(inv.getStackInSlot(0),Items.redstone) &&
                ItemStackHelper.hasType(inv.getStackInSlot(1),Items.redstone) &&
                ItemStackHelper.hasType(inv.getStackInSlot(2),Items.redstone) &&
                ItemStackHelper.hasType(inv.getStackInSlot(3),Items.redstone) &&
                ItemStackHelper.hasType(inv.getStackInSlot(4), AnimalNetItems.caughtEntityItem) &&
                ItemStackHelper.hasType(inv.getStackInSlot(5),Items.redstone) &&
                ItemStackHelper.hasType(inv.getStackInSlot(6),Items.redstone) &&
                ItemStackHelper.hasType(inv.getStackInSlot(7),Items.redstone) &&
                ItemStackHelper.hasType(inv.getStackInSlot(8),Items.redstone)) {
            match = inv.getStackInSlot(4).copy();

            if (match.getTagCompound().hasKey("age")) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
        ItemStack stack = match.copy();
        CaughtEntityItem.makeFakeChild(stack);
        return stack;
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return out;
    }
}
