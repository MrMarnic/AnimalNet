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
        getIngredients().add(Ingredient.of(new ItemStack(AnimalNetItems.SPAWNER_FRAGMENTAL_ITEM.get())));
        getIngredients().add(Ingredient.of(new ItemStack(AnimalNetItems.SPAWNER_FRAGMENTAL_ITEM.get())));
        getIngredients().add(Ingredient.of(new ItemStack(AnimalNetItems.SPAWNER_FRAGMENTAL_ITEM.get())));

        getIngredients().add(Ingredient.of(new ItemStack(AnimalNetItems.SPAWNER_FRAGMENTAL_ITEM.get())));
        getIngredients().add(Ingredient.of(RecipeUtil.getCaughtEntityFixedStack()));
        getIngredients().add(Ingredient.of(new ItemStack(AnimalNetItems.SPAWNER_FRAGMENTAL_ITEM.get())));

        getIngredients().add(Ingredient.of(new ItemStack(AnimalNetItems.SPAWNER_FRAGMENTAL_ITEM.get())));
        getIngredients().add(Ingredient.of(new ItemStack(AnimalNetItems.SPAWNER_FRAGMENTAL_ITEM.get())));
        getIngredients().add(Ingredient.of(new ItemStack(AnimalNetItems.SPAWNER_FRAGMENTAL_ITEM.get())));
    }

    private boolean isFragmental(int slot, IInventory inv) {
        return inv.getItem(slot).getItem().equals(AnimalNetItems.SPAWNER_FRAGMENTAL_ITEM.get());
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        if (isFragmental(0, inv) &&
                isFragmental(1, inv) &&
                isFragmental(2, inv) &&
                isFragmental(3, inv) &&
                RecipeUtil.isNetWithData(inv.getItem(4)) &&
                isFragmental(5, inv) &&
                isFragmental(6, inv) &&
                isFragmental(7, inv) &&
                isFragmental(8, inv)) {
            match = inv.getItem(4).copy();
            return true;
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        return SpawnerUtil.makeSpawnerCustom(match);
    }
}
