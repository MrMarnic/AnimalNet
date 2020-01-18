package me.marnic.animalnet.recipes;

import me.marnic.animalnet.api.RecipeUtil;
import me.marnic.animalnet.api.SpawnerUtil;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Copyright (c) 19.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class RecipeSpawner extends ShapedRecipe {

    private ItemStack match;

    public RecipeSpawner() {
        super(new ResourceLocation("animalnet", "fragmentals_to_spawner"), "", 3, 3, NonNullList.create(), new ItemStack(Blocks.SPAWNER));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));

        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
        getIngredients().add(Ingredient.fromStacks(RecipeUtil.getCaughtEntityFixedStack()));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));

        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
    }

    private boolean isFragmental(int slot, IInventory inv) {
        return inv.getStackInSlot(slot).getItem().equals(AnimalNetItems.spawnerFragmental);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        if (isFragmental(0, inv) &&
                isFragmental(1, inv) &&
                isFragmental(2, inv) &&
                isFragmental(3, inv) &&
                RecipeUtil.isNetWithData(inv.getStackInSlot(4)) &&
                isFragmental(5, inv) &&
                isFragmental(6, inv) &&
                isFragmental(7, inv) &&
                isFragmental(8, inv)) {
            match = inv.getStackInSlot(4).copy();
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack spawner = new ItemStack(Blocks.SPAWNER);
        SpawnerUtil.makeSpawnerCustom(spawner, new ResourceLocation(match.getTag().getString("animalName")));
        return spawner;
    }
}
