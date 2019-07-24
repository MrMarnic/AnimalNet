package me.marnic.animalnet.recipes;

import me.marnic.animalnet.api.RecipeUtil;
import me.marnic.animalnet.api.SpawnerUtil;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * Copyright (c) 19.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class RecipeSpawner extends ShapedRecipe {

    private ItemStack match;

    private boolean isFragmental(int slot, Inventory inv) {
        return inv.getInvStack(slot).getItem().equals(AnimalNetItems.spawnerFragmental);
    }

    public RecipeSpawner() {
        super(new Identifier("animalnet", "fragmentals_to_spawner"), "", 3, 3, DefaultedList.create(), new ItemStack(Blocks.SPAWNER));
        getPreviewInputs().add(Ingredient.ofItems(AnimalNetItems.spawnerFragmental));
        getPreviewInputs().add(Ingredient.ofItems(AnimalNetItems.spawnerFragmental));
        getPreviewInputs().add(Ingredient.ofItems(AnimalNetItems.spawnerFragmental));

        getPreviewInputs().add(Ingredient.ofItems(AnimalNetItems.spawnerFragmental));
        getPreviewInputs().add(Ingredient.ofItems(AnimalNetItems.caughtEntityItem));
        getPreviewInputs().add(Ingredient.ofItems(AnimalNetItems.spawnerFragmental));

        getPreviewInputs().add(Ingredient.ofItems(AnimalNetItems.spawnerFragmental));
        getPreviewInputs().add(Ingredient.ofItems(AnimalNetItems.spawnerFragmental));
        getPreviewInputs().add(Ingredient.ofItems(AnimalNetItems.spawnerFragmental));
    }

    @Override
    public boolean matches(CraftingInventory inv, World var2) {
        if (isFragmental(0, inv) &&
                isFragmental(1, inv) &&
                isFragmental(2, inv) &&
                isFragmental(3, inv) &&
                RecipeUtil.isNetWithData(inv.getInvStack(4)) &&
                isFragmental(5, inv) &&
                isFragmental(6, inv) &&
                isFragmental(7, inv) &&
                isFragmental(8, inv)) {
            match = inv.getInvStack(4).copy();
            return true;
        }
        return false;
    }

    @Override
    public ItemStack craft(CraftingInventory var1) {
        ItemStack spawner = new ItemStack(Blocks.SPAWNER);
        SpawnerUtil.makeSpawnerCustom(spawner, new Identifier(match.getTag().getString("animalName")));
        return spawner;
    }
}
