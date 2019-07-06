package me.marnic.animalnet.common.recipes;

import me.marnic.animalnet.api.RecipeUtil;
import me.marnic.animalnet.common.item.ItemCaughtEntity;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
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
public class RecipeAnimalToChild extends ShapedRecipes {

    private ItemStack match;

    public RecipeAnimalToChild() {
        super("", 3, 3, NonNullList.create(), RecipeUtil.getCaughtEntityFixedStack());
        getIngredients().add(Ingredient.fromItem(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItem(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItem(Items.REDSTONE));

        getIngredients().add(Ingredient.fromItem(Items.REDSTONE));
        getIngredients().add(IngredientNBT.fromStacks(RecipeUtil.getCaughtEntityFixedStack()));
        getIngredients().add(Ingredient.fromItem(Items.REDSTONE));

        getIngredients().add(Ingredient.fromItem(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItem(Items.REDSTONE));
        getIngredients().add(Ingredient.fromItem(Items.REDSTONE));
        setRegistryName("animalnet", "caught_animal_to_child");
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        if (inv.getStackInSlot(0).getItem().equals(Items.REDSTONE) &&
                inv.getStackInSlot(1).getItem().equals(Items.REDSTONE) &&
                inv.getStackInSlot(2).getItem().equals(Items.REDSTONE) &&
                inv.getStackInSlot(3).getItem().equals(Items.REDSTONE) &&
                RecipeUtil.isNetWithData(inv.getStackInSlot(4)) &&
                inv.getStackInSlot(5).getItem().equals(Items.REDSTONE) &&
                inv.getStackInSlot(6).getItem().equals(Items.REDSTONE) &&
                inv.getStackInSlot(7).getItem().equals(Items.REDSTONE) &&
                inv.getStackInSlot(8).getItem().equals(Items.REDSTONE)) {
            match = inv.getStackInSlot(4).copy();

            if (match.getTagCompound().hasKey("age")) {
                return true;
            }

            return false;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack = match.copy();
        ItemCaughtEntity.makeFakeChild(stack);
        return stack;
    }
}
