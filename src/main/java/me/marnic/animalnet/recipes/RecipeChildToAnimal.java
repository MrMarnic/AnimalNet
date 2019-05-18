package me.marnic.animalnet.recipes;

import me.marnic.animalnet.items.CaughtEntityItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
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
    public boolean matches(IInventory inv, World worldIn) {
        if(inv.getStackInSlot(0).getItem().equals(Items.BONE_MEAL)&&
                inv.getStackInSlot(1).getItem().equals(Items.BONE_MEAL)&&
                inv.getStackInSlot(2).getItem().equals(Items.BONE_MEAL)&&
                inv.getStackInSlot(3).getItem().equals(Items.BONE_MEAL)&&
                inv.getStackInSlot(4).getItem().equals(AnimalNetItems.caughtEntityItem)&&
                inv.getStackInSlot(5).getItem().equals(Items.BONE_MEAL)&&
                inv.getStackInSlot(6).getItem().equals(Items.BONE_MEAL)&&
                inv.getStackInSlot(7).getItem().equals(Items.BONE_MEAL)&&
                inv.getStackInSlot(8).getItem().equals(Items.BONE_MEAL)) {
            match = inv.getStackInSlot(4).copy();

            if(match.getTag().hasKey("age")) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack stack = match.copy();
        CaughtEntityItem.makeFakeAdult(stack);
        return stack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width>=3 && height>=3;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return out;
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("animalnet:caught_child_to_adult");
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializers.CRAFTING_SHAPED;
    }
}
