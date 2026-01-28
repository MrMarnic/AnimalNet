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
public class RecipeAnimalToChild extends ShapedRecipe {

    private ItemStack match;


    public RecipeAnimalToChild() {
        super(new ResourceLocation("animalnet:caught_animal_to_child"), "", 3, 3, NonNullList.create(), RecipeUtil.getCaughtEntityFixedStack());
        getIngredients().add(Ingredient.of(Items.REDSTONE));
        getIngredients().add(Ingredient.of(Items.REDSTONE));
        getIngredients().add(Ingredient.of(Items.REDSTONE));
        getIngredients().add(Ingredient.of(Items.REDSTONE));
        getIngredients().add(Ingredient.of(RecipeUtil.getCaughtEntityFixedStack()));
        getIngredients().add(Ingredient.of(Items.REDSTONE));
        getIngredients().add(Ingredient.of(Items.REDSTONE));
        getIngredients().add(Ingredient.of(Items.REDSTONE));
        getIngredients().add(Ingredient.of(Items.REDSTONE));
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        if (inv.getItem(0).getItem().equals(Items.REDSTONE) &&
                inv.getItem(1).getItem().equals(Items.REDSTONE) &&
                inv.getItem(2).getItem().equals(Items.REDSTONE) &&
                inv.getItem(3).getItem().equals(Items.REDSTONE) &&
                RecipeUtil.isNetWithData(inv.getItem(4)) &&
                inv.getItem(5).getItem().equals(Items.REDSTONE) &&
                inv.getItem(6).getItem().equals(Items.REDSTONE) &&
                inv.getItem(7).getItem().equals(Items.REDSTONE) &&
                inv.getItem(8).getItem().equals(Items.REDSTONE)) {
            match = inv.getItem(4).copy();

            return CaughtEntityItem.hasAge(match);

        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        ItemStack stack = match.copy();
        CaughtEntityItem.makeChild(stack);
        return stack;
    }
}
