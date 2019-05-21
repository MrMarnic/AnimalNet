package me.marnic.animalnet.recipes;

import me.marnic.animalnet.api.RecipeUtil;
import me.marnic.animalnet.api.SpawnerUtil;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Copyright (c) 19.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class RecipeSpawner extends ShapedRecipes {

    private ItemStack match;

    private boolean isFragmental(int slot, InventoryCrafting inv) {
        return inv.getStackInSlot(slot).getItem().equals(AnimalNetItems.spawnerFragmental);
    }

    public RecipeSpawner() {
        super("", 3, 3, NonNullList.create(), new ItemStack(Blocks.MOB_SPAWNER));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));

        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
        getIngredients().add(Ingredient.fromStacks(RecipeUtil.getCaughtEntityFixedStack()));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));

        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));
        getIngredients().add(Ingredient.fromStacks(new ItemStack(AnimalNetItems.spawnerFragmental)));

        setRegistryName("animalnet", "fragmentals_to_spawner");
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
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
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack spawner = new ItemStack(Blocks.MOB_SPAWNER);
        SpawnerUtil.makeSpawnerCustom(spawner, new ResourceLocation(match.getTagCompound().getString("animalName")));
        return spawner;
    }
}
