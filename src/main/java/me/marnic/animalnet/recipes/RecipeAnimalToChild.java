package me.marnic.animalnet.recipes;


import me.marnic.animalnet.items.CaughtEntityItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * Copyright (c) 18.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class RecipeAnimalToChild extends ShapedRecipe {

    private ItemStack match;

    private final ItemStack out = new ItemStack(AnimalNetItems.caughtEntityItem);

    public RecipeAnimalToChild() {
        super(new Identifier("animalnet", "caught_animal_to_child"), "", 3, 3, DefaultedList.create(), new ItemStack(AnimalNetItems.caughtEntityItem));
        getPreviewInputs().add(Ingredient.ofItems(Items.REDSTONE));
        getPreviewInputs().add(Ingredient.ofItems(Items.REDSTONE));
        getPreviewInputs().add(Ingredient.ofItems(Items.REDSTONE));
        getPreviewInputs().add(Ingredient.ofItems(Items.REDSTONE));
        getPreviewInputs().add(Ingredient.ofItems(AnimalNetItems.caughtEntityItem));
        getPreviewInputs().add(Ingredient.ofItems(Items.REDSTONE));
        getPreviewInputs().add(Ingredient.ofItems(Items.REDSTONE));
        getPreviewInputs().add(Ingredient.ofItems(Items.REDSTONE));
        getPreviewInputs().add(Ingredient.ofItems(Items.REDSTONE));
    }

    @Override
    public boolean matches(CraftingInventory inv, World var2) {
        if (inv.getInvStack(0).getItem().equals(Items.REDSTONE) &&
                inv.getInvStack(1).getItem().equals(Items.REDSTONE) &&
                inv.getInvStack(2).getItem().equals(Items.REDSTONE) &&
                inv.getInvStack(3).getItem().equals(Items.REDSTONE) &&
                inv.getInvStack(4).getItem().equals(AnimalNetItems.caughtEntityItem) &&
                inv.getInvStack(5).getItem().equals(Items.REDSTONE) &&
                inv.getInvStack(6).getItem().equals(Items.REDSTONE) &&
                inv.getInvStack(7).getItem().equals(Items.REDSTONE) &&
                inv.getInvStack(8).getItem().equals(Items.REDSTONE)) {
            match = inv.getInvStack(4).copy();

            return match.getTag().containsKey("age");

        }
        return false;
    }

    @Override
    public ItemStack craft(CraftingInventory var1) {
        ItemStack stack = match.copy();
        CaughtEntityItem.makeFakeChild(stack);
        return stack;
    }
}
