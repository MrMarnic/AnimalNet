package me.marnic.animalnet.mechanics;

import com.google.common.collect.ImmutableMap;
import me.marnic.animalnet.items.CaughtEntityItem;
import me.marnic.animalnet.main.AnimalNetItems;
import me.marnic.animalnet.recipes.RecipeAnimalToChild;
import me.marnic.animalnet.recipes.RecipeChildToAnimal;
import me.marnic.animalnet.recipes.RecipeSpawner;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 18.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ServerHandler {
    public static void handleServerStarting(MinecraftServer server) {
        for (Field f : FieldUtils.getAllFields(RecipeManager.class)) {
            if (f.getType().equals(Map.class)) {
                Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes;

                ReflectionUtil.makeAccessible(f);

                recipes = (Map<RecipeType<?>, Map<Identifier, Recipe<?>>>) ReflectionUtil.getFieldValue(f, server.getRecipeManager());

                Recipe toChild = new RecipeAnimalToChild();
                Recipe toAnimal = new RecipeChildToAnimal();
                Recipe spawner = new RecipeSpawner();

                HashMap<RecipeType<?>, Map<Identifier, Recipe<?>>> recipeMut = new HashMap<>(recipes);
                HashMap<Identifier, Recipe> craftingRecipesMut = new HashMap<>(recipeMut.get(RecipeType.CRAFTING));


                craftingRecipesMut.put(toChild.getId(), toChild);
                craftingRecipesMut.put(toAnimal.getId(), toAnimal);
                craftingRecipesMut.put(spawner.getId(), spawner);


                recipeMut.put(RecipeType.CRAFTING, (Map) craftingRecipesMut);

                ReflectionUtil.setFieldValue(f, server.getRecipeManager(), new ImmutableMap.Builder().putAll(recipeMut).build());
            }
        }
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
