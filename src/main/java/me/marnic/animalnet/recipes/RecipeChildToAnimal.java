package me.marnic.animalnet.recipes;

import me.marnic.animalnet.api.RecipeUtil;
import me.marnic.animalnet.items.CaughtEntityItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Copyright (c) 18.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class RecipeChildToAnimal extends ShapedRecipe {

    private ItemStack match;

    public RecipeChildToAnimal() {
        super(new ResourceLocation("animalnet:caught_child_to_adult"), "", 3, 3, NonNullList.create(), RecipeUtil.getCaughtEntityFixedStack());
        getIngredients().add(Ingredient.fromItems(Items.BONE_MEAL));
        getIngredients().add(Ingredient.fromItems(Items.BONE_MEAL));
        getIngredients().add(Ingredient.fromItems(Items.BONE_MEAL));
        getIngredients().add(Ingredient.fromItems(Items.BONE_MEAL));
        getIngredients().add(Ingredient.fromStacks(RecipeUtil.getCaughtEntityFixedStack()));
        getIngredients().add(Ingredient.fromItems(Items.BONE_MEAL));
        getIngredients().add(Ingredient.fromItems(Items.BONE_MEAL));
        getIngredients().add(Ingredient.fromItems(Items.BONE_MEAL));
        getIngredients().add(Ingredient.fromItems(Items.BONE_MEAL));
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        if (inv.getStackInSlot(0).getItem().equals(Items.BONE_MEAL) &&
                inv.getStackInSlot(1).getItem().equals(Items.BONE_MEAL) &&
                inv.getStackInSlot(2).getItem().equals(Items.BONE_MEAL) &&
                inv.getStackInSlot(3).getItem().equals(Items.BONE_MEAL) &&
                RecipeUtil.isNetWithData(inv.getStackInSlot(4)) &&
                inv.getStackInSlot(5).getItem().equals(Items.BONE_MEAL) &&
                inv.getStackInSlot(6).getItem().equals(Items.BONE_MEAL) &&
                inv.getStackInSlot(7).getItem().equals(Items.BONE_MEAL) &&
                inv.getStackInSlot(8).getItem().equals(Items.BONE_MEAL)) {
            match = inv.getStackInSlot(4).copy();

            return match.getTag().contains("age");
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack stack = match.copy();
        CaughtEntityItem.makeFakeAdult(stack);
        return stack;
    }
}
