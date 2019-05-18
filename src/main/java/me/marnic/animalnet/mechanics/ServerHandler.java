package me.marnic.animalnet.mechanics;

import me.marnic.animalnet.items.CaughtEntityItem;
import me.marnic.animalnet.main.AnimalNetItems;
import me.marnic.animalnet.recipes.RecipeAnimalToChild;
import me.marnic.animalnet.recipes.RecipeChildToAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.crafting.ShapedRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DefaultedList;
import net.minecraft.world.dimension.DimensionType;

/**
 * Copyright (c) 18.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ServerHandler {
    public static void handleServerStarting(MinecraftServer server) {
        server.getRecipeManager().add(new RecipeAnimalToChild());
        server.getRecipeManager().add(new RecipeChildToAnimal());
    }

    public static void handleOnCrafted(ItemStack stack, PlayerEntity playerEntity) {
        if(!playerEntity.world.isClient) {
            if (stack.getItem().equals(AnimalNetItems.caughtEntityItem)) {
                if (stack.getTag().getString("age").equalsIgnoreCase("Adult")) {
                    CaughtEntityItem.makeAdult(stack,playerEntity.world);
                }
                else {
                    CaughtEntityItem.makeChild(stack,playerEntity.world);
                }
            }
        }
    }
}
