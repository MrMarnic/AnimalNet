package me.marnic.animalnet.recipes;

import me.marnic.animalnet.api.ItemStackHelper;
import me.marnic.animalnet.item.AnimalNetItem;
import me.marnic.animalnet.item.CaughtEntityItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/**
 * Copyright (c) 05.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CaughtAnimalToAdultRecipe implements IRecipe {

    private ItemStack match;

    private final ItemStack out = new ItemStack(AnimalNetItems.caughtEntityItem);

    @Override
    public boolean matches(InventoryCrafting inv, World p_77569_2_) {
            if (ItemStackHelper.hasType(inv.getStackInSlot(0),Items.dye) &&
                    ItemStackHelper.hasType(inv.getStackInSlot(1),Items.dye) &&
                    ItemStackHelper.hasType(inv.getStackInSlot(2),Items.dye) &&
                    ItemStackHelper.hasType(inv.getStackInSlot(3),Items.dye) &&
                    ItemStackHelper.hasType(inv.getStackInSlot(4), AnimalNetItems.caughtEntityItem) &&
                    ItemStackHelper.hasType(inv.getStackInSlot(5),Items.dye) &&
                    ItemStackHelper.hasType(inv.getStackInSlot(6),Items.dye) &&
                    ItemStackHelper.hasType(inv.getStackInSlot(7),Items.dye) &&
                    ItemStackHelper.hasType(inv.getStackInSlot(8),Items.dye)) {
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
        CaughtEntityItem.makeFakeAdult(stack);
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
