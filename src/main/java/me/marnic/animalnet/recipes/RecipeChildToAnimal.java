package me.marnic.animalnet.recipes;

import me.marnic.animalnet.item.CaughtEntityItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Copyright (c) 18.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class RecipeChildToAnimal implements IRecipe {
    private ItemStack match;

    private final ItemStack out = new ItemStack(AnimalNetItems.caughtEntityItem);

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        if(inv.getStackInSlot(0).getItem().equals(Items.DYE)&&
                inv.getStackInSlot(1).getItem().equals(Items.DYE)&&
                inv.getStackInSlot(2).getItem().equals(Items.DYE)&&
                inv.getStackInSlot(3).getItem().equals(Items.DYE)&&
                inv.getStackInSlot(4).getItem().equals(AnimalNetItems.caughtEntityItem)&&
                inv.getStackInSlot(5).getItem().equals(Items.DYE)&&
                inv.getStackInSlot(6).getItem().equals(Items.DYE)&&
                inv.getStackInSlot(7).getItem().equals(Items.DYE)&&
                inv.getStackInSlot(8).getItem().equals(Items.DYE)) {
            match = inv.getStackInSlot(4).copy();

            if(match.getTagCompound().hasKey("age")) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack = match.copy();
        CaughtEntityItem.makeFakeAdult(stack);
        return stack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return out;
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        return this;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation("animalnet:caught_animal");
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return null;
    }
}
