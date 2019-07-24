package me.marnic.animalnet.common.recipes;

import me.marnic.animalnet.api.RecipeUtil;
import me.marnic.animalnet.common.item.ItemCaughtEntity;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IngredientNBT;

/**
 * Copyright (c) 18.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class RecipeChildToAnimal extends ShapedRecipes {
    private ItemStack match;

    public RecipeChildToAnimal() {
        super("", 3, 3, NonNullList.create(), RecipeUtil.getCaughtEntityFixedStack());
        getIngredients().add(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)));

        getIngredients().add(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)));
        getIngredients().add(IngredientNBT.fromStacks(RecipeUtil.getCaughtEntityFixedStack()));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)));

        getIngredients().add(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)));
        setRegistryName("animalnet", "caught_child_to_adult");
    }

    private boolean isBoneMeal(int slot, InventoryCrafting inv) {
        return inv.getStackInSlot(slot).getItem().equals(Items.DYE) && EnumDyeColor.byDyeDamage(inv.getStackInSlot(slot).getMetadata()) == EnumDyeColor.WHITE;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        if (isBoneMeal(0, inv) &&
                isBoneMeal(1, inv) &&
                isBoneMeal(2, inv) &&
                isBoneMeal(3, inv) &&
                RecipeUtil.isNetWithData(inv.getStackInSlot(4)) &&
                isBoneMeal(5, inv) &&
                isBoneMeal(6, inv) &&
                isBoneMeal(7, inv) &&
                isBoneMeal(8, inv)) {
            match = inv.getStackInSlot(4).copy();

            return match.getTagCompound().hasKey("age");
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack = match.copy();
        ItemCaughtEntity.makeFakeAdult(stack);
        return stack;
    }
}
