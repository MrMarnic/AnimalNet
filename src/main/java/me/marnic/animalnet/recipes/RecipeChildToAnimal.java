package me.marnic.animalnet.recipes;

import me.marnic.animalnet.item.CaughtEntityItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Copyright (c) 18.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class RecipeChildToAnimal implements IShapedRecipe {
    private ItemStack match;

    private final ItemStack out = new ItemStack(AnimalNetItems.caughtEntityItem);

    private NonNullList<Ingredient> ingredients;

    public RecipeChildToAnimal() {
        ingredients = NonNullList.create();
        ingredients.add(Ingredient.fromStacks(new ItemStack(Items.DYE,1,15)));
        ingredients.add(Ingredient.fromStacks(new ItemStack(Items.DYE,1,15)));
        ingredients.add(Ingredient.fromStacks(new ItemStack(Items.DYE,1,15)));

        ingredients.add(Ingredient.fromStacks(new ItemStack(Items.DYE,1,15)));
        ingredients.add(Ingredient.fromItem(AnimalNetItems.caughtEntityItem));
        ingredients.add(Ingredient.fromStacks(new ItemStack(Items.DYE,1,15)));

        ingredients.add(Ingredient.fromStacks(new ItemStack(Items.DYE,1,15)));
        ingredients.add(Ingredient.fromStacks(new ItemStack(Items.DYE,1,15)));
        ingredients.add(Ingredient.fromStacks(new ItemStack(Items.DYE,1,15)));
    }

    private boolean isBoneMeal(int slot,InventoryCrafting inv) {
        return inv.getStackInSlot(slot).getItem().equals(Items.DYE) && EnumDyeColor.byDyeDamage(inv.getStackInSlot(slot).getMetadata())==EnumDyeColor.WHITE;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        if(isBoneMeal(0,inv)&&
                isBoneMeal(1,inv)&&
                isBoneMeal(2,inv)&&
                isBoneMeal(3,inv)&&
                inv.getStackInSlot(4).getItem().equals(AnimalNetItems.caughtEntityItem)&&
                isBoneMeal(5,inv)&&
                isBoneMeal(6,inv)&&
                isBoneMeal(7,inv)&&
                isBoneMeal(8,inv)) {
            match = inv.getStackInSlot(4).copy();

            if(match.getTagCompound().hasKey("age")) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack = match.copy();
        CaughtEntityItem.makeFakeAdult(stack);
        return stack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return out;
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        return this;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation("animalnet:caught_animal");
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return null;
    }

    @Override
    public int getRecipeHeight() {
        return 3;
    }

    @Override
    public int getRecipeWidth() {
        return 3;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }
}
