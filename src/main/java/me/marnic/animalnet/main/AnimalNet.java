package me.marnic.animalnet.main;

import com.google.common.collect.ImmutableMap;
import me.marnic.animalnet.config.AnimalNetConfig;
import me.marnic.animalnet.recipes.RecipeAnimalToChild;
import me.marnic.animalnet.recipes.RecipeChildToAnimal;
import me.marnic.animalnet.recipes.RecipeSpawner;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mod("animalnet")
public class AnimalNet {

    public static AnimalNetModHandler modHandler;
    public static final String MODID = "animalnet";

    public AnimalNet() {
        MinecraftForge.EVENT_BUS.register(modHandler = new AnimalNetModHandler());
        MinecraftForge.EVENT_BUS.register(this);
        AnimalNetConfig.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AnimalNetConfig.SPEC);
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent e) {
        try {
            Field field = getFieldForTypeInClass(Map.class, RecipeManager.class);

            Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes;

            ReflectionUtil.makeAccessible(field);

            recipes = (Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>)ReflectionUtil.getFieldValue(field,e.getServer().getRecipeManager());

            IRecipe toChild = new RecipeAnimalToChild();
            IRecipe toAnimal = new RecipeChildToAnimal();
            IRecipe spawner = new RecipeSpawner();

            HashMap<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipeMut = new HashMap<>(recipes);
            HashMap<ResourceLocation, IRecipe> craftingRecipesMut = new HashMap<>(recipeMut.get(IRecipeType.CRAFTING));


            craftingRecipesMut.put(toChild.getId(),toChild);
            craftingRecipesMut.put(toAnimal.getId(),toAnimal);
            craftingRecipesMut.put(spawner.getId(),spawner);


            recipeMut.put(IRecipeType.CRAFTING,(Map)craftingRecipesMut);

            ReflectionUtil.setFieldValue(field,e.getServer().getRecipeManager(),new ImmutableMap.Builder().putAll(recipeMut).build());

        }catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    private Field getFieldForTypeInClass(Class type,Class loc) {
        for(Field f:FieldUtils.getAllFields(loc)) {
            if(f.getType().equals(type)) {
                return f;
            }
        }
        return null;
    }
}
