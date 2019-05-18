package me.marnic.animalnet.recipes;

import com.google.gson.JsonObject;
import me.marnic.animalnet.items.CaughtEntityItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Copyright (c) 18.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class RecipeAnimalToChild extends ShapedRecipe {

    private ItemStack match;


    public RecipeAnimalToChild() {
        super(new ResourceLocation("animalnet:caught_animal_to_child"), "", 3, 3, NonNullList.create(), new ItemStack(AnimalNetItems.caughtEntityItem));
        getIngredients().add(Ingredient.fromItems(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItems(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItems(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItems(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItems(AnimalNetItems.caughtEntityItem));
        getIngredients().add(Ingredient.fromItems(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItems(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItems(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItems(Items.REDSTONE));
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        if(inv.getStackInSlot(0).getItem().equals(Items.REDSTONE)&&
                inv.getStackInSlot(1).getItem().equals(Items.REDSTONE)&&
                inv.getStackInSlot(2).getItem().equals(Items.REDSTONE)&&
                inv.getStackInSlot(3).getItem().equals(Items.REDSTONE)&&
                inv.getStackInSlot(4).getItem().equals(AnimalNetItems.caughtEntityItem)&&
                inv.getStackInSlot(5).getItem().equals(Items.REDSTONE)&&
                inv.getStackInSlot(6).getItem().equals(Items.REDSTONE)&&
                inv.getStackInSlot(7).getItem().equals(Items.REDSTONE)&&
                inv.getStackInSlot(8).getItem().equals(Items.REDSTONE)) {
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
        CaughtEntityItem.makeFakeChild(stack);
        return stack;
    }
}
