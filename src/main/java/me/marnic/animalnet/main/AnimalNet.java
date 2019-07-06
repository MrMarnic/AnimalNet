package me.marnic.animalnet.main;

import me.marnic.animalnet.config.AnimalNetConfig;
import me.marnic.animalnet.recipes.RecipeAnimalToChild;
import me.marnic.animalnet.recipes.RecipeChildToAnimal;
import me.marnic.animalnet.recipes.RecipeSpawner;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

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
        e.getServer().getRecipeManager().addRecipe(new RecipeChildToAnimal());
        e.getServer().getRecipeManager().addRecipe(new RecipeAnimalToChild());
        e.getServer().getRecipeManager().addRecipe(new RecipeSpawner());
    }
}
